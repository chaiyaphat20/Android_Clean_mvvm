package com.example.cleanarchitecturemvvmandroid.domain.repository

import com.example.cleanarchitecturemvvmandroid.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun refreshUsers()
    suspend fun getUserById(id: Int): User?
}