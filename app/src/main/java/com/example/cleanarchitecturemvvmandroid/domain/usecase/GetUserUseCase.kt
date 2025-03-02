package com.example.cleanarchitecturemvvmandroid.domain.usecase

import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.domain.repository.UserRepository
import com.example.cleanarchitecturemvvmandroid.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Response<List<User>>> {
        return userRepository.getUsers()
    }
}

class RefreshUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.refreshUsers()
    }
}