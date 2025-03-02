package com.example.cleanarchitecturemvvmandroid.presentation.user


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturemvvmandroid.data.local.dao.UserDao
import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.domain.usecase.GetUsersUseCase
import com.example.cleanarchitecturemvvmandroid.domain.usecase.RefreshUsersUseCase
import com.example.cleanarchitecturemvvmandroid.utils.Response
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
    private val refreshUsersUseCase: RefreshUsersUseCase,
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            // ติดตามข้อมูลผู้ใช้จาก database ผ่าน Flow
            getUsersUseCase().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(isLoading = true)
                        }
                    }
                    is Response.Success -> {
                        _state.update { currentState ->
                            currentState.copy(
                                users = response.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Response.Error -> {
                        _state.update { currentState ->
                            // ใช้ข้อมูลที่มีอยู่ (fallback data) ถ้ามี
                            val updatedUsers = response.data ?: currentState.users
                            currentState.copy(
                                users = updatedUsers,
                                isLoading = false,
                                error = response.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }


            // ดึงข้อมูลจาก API เพื่ออัพเดตฐานข้อมูล
//            refreshUsers()
        }
    }

    fun saveUsers() {
        viewModelScope.launch {
            try {
                userDao.insertAll(state.value.users.map { it.toUserEntity() })
                // แสดงข้อความแจ้งเตือนว่าบันทึกข้อมูลสำเร็จ
            } catch (e: Exception) {
                // แสดงข้อความแจ้งเตือนว่าบันทึกข้อมูลล้มเหลว
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                userDao.deleteAllUsers()
                // Show a success message or update the UI accordingly
            } catch (e: Exception) {
                // Show an error message or handle the exception
            }
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