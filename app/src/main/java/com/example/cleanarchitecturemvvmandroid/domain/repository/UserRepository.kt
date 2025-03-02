package com.example.cleanarchitecturemvvmandroid.domain.repository

import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // คืนค่าเป็น Flow<Response<List<User>>> แทน Flow<List<User>>
    // เพื่อให้สามารถจัดการสถานะ Loading, Success, Error ได้
    fun getUsers(): Flow<Response<List<User>>>

    suspend fun refreshUsers()
    suspend fun getUserById(id: Int): User?
    suspend fun getUsersFromDatabase(): List<User>
}