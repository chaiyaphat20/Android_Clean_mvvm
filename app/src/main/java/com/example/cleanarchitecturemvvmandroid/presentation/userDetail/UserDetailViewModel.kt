package com.example.cleanarchitecturemvvmandroid.presentation.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UserDetailState>(UserDetailState.Loading)
    val userState: StateFlow<UserDetailState> = _userState.asStateFlow()

    fun getUserDetail(userId: Int) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                _userState.value = if (user != null) {
                    UserDetailState.Success(user)
                } else {
                    UserDetailState.Error("User not found")
                }
            } catch (e: Exception) {
                _userState.value = UserDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class UserDetailState {
    object Loading : UserDetailState()
    data class Success(val user: User) : UserDetailState()
    data class Error(val message: String) : UserDetailState()
}