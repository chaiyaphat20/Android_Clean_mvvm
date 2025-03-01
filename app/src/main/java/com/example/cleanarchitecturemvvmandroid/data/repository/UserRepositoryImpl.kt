package com.example.cleanarchitecturemvvmandroid.data.repository

import com.example.cleanarchitecturemvvmandroid.data.local.dao.UserDao
import com.example.cleanarchitecturemvvmandroid.data.local.entity.UserEntity
import com.example.cleanarchitecturemvvmandroid.data.remote.api.UserApi
import com.example.cleanarchitecturemvvmandroid.data.remote.model.UserResponse
import com.example.cleanarchitecturemvvmandroid.domain.model.Address
import com.example.cleanarchitecturemvvmandroid.domain.model.Company
import com.example.cleanarchitecturemvvmandroid.domain.model.Geo
import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.example.cleanarchitecturemvvmandroid.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<List<User>> = flow {
        val userResponses = userApi.getUsers()
        val users = userResponses.map { it.toUser() }
        emit(users)
    }

    override suspend fun refreshUsers() {
        try {
            val remoteUsers = userApi.getUsers()
            userDao.insertAll(remoteUsers.map { it.toEntity() })
        } catch (e: Exception) {
            // Handle errors (could throw or log)
            e.printStackTrace()
        }
    }

    override suspend fun getUserById(id: Int): User? {
        return userDao.getById(id)?.toDomainModel()
    }

    private fun UserEntity.toDomainModel(): User {
        return User(
            id = id,
            name = name,
            username = username,
            email = email,
            phone = phone,
            website = website,
            address = Address(
                street = street,
                suite = suite,
                city = city,
                zipcode = zipcode,
                geo = Geo(
                    lat = lat,
                    lng = lng
                )
            ),
            company = Company(
                name = companyName,
                catchPhrase = companyCatchPhrase,
                bs = companyBs
            )
        )
    }

    // ใน UserRepositoryImpl เพิ่มฟังก์ชันแมปปิ้งระหว่าง Response และ Entity
    private fun UserResponse.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            username = username,
            email = email,
            phone = phone,
            website = website,
            street = address.street,
            suite = address.suite,
            city = address.city,
            zipcode = address.zipcode,
            lat = address.geo.lat,
            lng = address.geo.lng,
            companyName = company.name,
            companyCatchPhrase = company.catchPhrase,
            companyBs = company.bs
        )
    }

    override suspend fun getUsersFromDatabase(): List<User> {
        val data = userDao.getAll()
            .map { entities -> entities.map { it.toUser() } }
            .flowOn(Dispatchers.IO)
            .first()
        return data
    }
}