package com.example.cleanarchitecturemvvmandroid.presentation.userdatabase
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
class UserDatabaseViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserDatabaseState())
    val state: StateFlow<UserDatabaseState> = _state.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            try {
                _state.value = UserDatabaseState(isLoading = true)
                val users = userRepository.getUsersFromDatabase()
                _state.value = UserDatabaseState(users = users)
            } catch (e: Exception) {
                _state.value = UserDatabaseState(error = e.message ?: "Unknown error")
            }
        }
    }
}

data class UserDatabaseState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)