package com.example.cleanarchitecturemvvmandroid.presentation.user


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.domain.usecase.GetUsersUseCase
import com.example.cleanarchitecturemvvmandroid.domain.usecase.RefreshUsersUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * ViewModel สำหรับหน้าแสดงรายการผู้ใช้
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            // ติดตามข้อมูลผู้ใช้จาก database ผ่าน Flow
            getUsersUseCase().collect { users ->
                _state.update { currentState ->
                    Log.d("ART555",Gson().toJson(users))
                    currentState.copy(users = users)
                }
            }

            // ดึงข้อมูลจาก API เพื่ออัพเดตฐานข้อมูล
            refreshUsers()
        }
    }

    private fun refreshUsers() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                refreshUsersUseCase()
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Unknown error occurred")
                }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class UserState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)