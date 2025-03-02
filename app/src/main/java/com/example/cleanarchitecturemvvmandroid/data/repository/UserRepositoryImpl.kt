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
import com.example.cleanarchitecturemvvmandroid.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.random.Random
import retrofit2.Response as RetrofitResponse

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao
) : UserRepository {

     fun createHttpException(code: Int, message: String): HttpException {
        val responseBody = message.toResponseBody("text/plain".toMediaTypeOrNull())
        return HttpException(RetrofitResponse.error<Any>(code, responseBody))
    }

    fun Throwable.toErrorMessage(): String {
        return when (this) {
            is HttpException -> {
                when (code()) {
                    404 -> "ไม่พบข้อมูลที่ร้องขอ (404)"
                    500 -> "เซิร์ฟเวอร์เกิดข้อผิดพลาด (500)"
                    else -> "เกิดข้อผิดพลาด HTTP: ${code()}"
                }
            }
            is SocketTimeoutException -> "การเชื่อมต่อใช้เวลานานเกินไป โปรดลองอีกครั้ง"
            is UnknownHostException -> "ไม่สามารถเชื่อมต่อกับเซิร์ฟเวอร์ได้ โปรดตรวจสอบการเชื่อมต่อของคุณ"
            is IOException -> "เกิดข้อผิดพลาดในการเชื่อมต่อเครือข่าย"
            else -> "เกิดข้อผิดพลาดที่ไม่คาดคิด: ${message}"
        }
    }

    override fun getUsers(): Flow<Response<List<User>>> = flow {
        emit(Response.Loading())
        try {
//            val random = Random.nextInt(5)
//            when (random) {
//                0 -> throw createHttpException(404, "Not Found")
//                1 -> throw createHttpException(500, "Internal Server Error")
//                2 -> throw SocketTimeoutException("Request timed out")
//                3 -> throw UnknownHostException("Unable to resolve host")
//                4 -> throw IOException("Network connection failed")
//                else -> {
//                    // กรณีไม่มีข้อผิดพลาด
//                    val userResponses = userApi.getUsers()
//                    val users = userResponses.map { it.toUser() }
//                    emit(Response.Success(users))
//                }
//            }

            val userResponses = userApi.getUsers()
            val users = userResponses.map { it.toUser() }
            emit(Response.Success(users))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error(error = e, message = e.toErrorMessage()))
        }
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