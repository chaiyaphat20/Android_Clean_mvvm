package com.example.cleanarchitecturemvvmandroid.di

import android.content.Context
import androidx.room.Room
import com.example.cleanarchitecturemvvmandroid.data.local.AppDatabase
import com.example.cleanarchitecturemvvmandroid.data.local.dao.UserDao
import com.example.cleanarchitecturemvvmandroid.data.remote.api.UserApi
import com.example.cleanarchitecturemvvmandroid.data.repository.UserRepositoryImpl
import com.example.cleanarchitecturemvvmandroid.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * AppModule เป็นคลาสที่ใช้สำหรับกำหนดวิธีการสร้าง dependencies ต่างๆ ในแอพ
 * โดยใช้ Dagger Hilt เป็นเครื่องมือในการทำ Dependency Injection
 */
@Module // บอกว่าคลาสนี้เป็น Module สำหรับ Dagger Hilt
@InstallIn(SingletonComponent::class) // บอกว่า dependencies ใน Module นี้จะมีอายุเท่ากับแอพ
object AppModule {

    //--DEFAULT ต้องเขียน--//
    /**
     * สร้าง OkHttpClient สำหรับการเชื่อมต่อกับ API
     *
     * OkHttpClient เป็นไลบรารีที่ใช้จัดการการเชื่อมต่อ HTTP
     * เราตั้งค่า logging เพื่อให้เห็นข้อมูลการรับส่งข้อมูลกับ API (ช่วยในการ debug)
     */
    @Provides // บอกว่าเมธอดนี้จะ provide dependency
    @Singleton // บอกว่าจะสร้าง instance เดียวตลอดอายุของแอพ
    fun provideOkHttpClient(): OkHttpClient {
        // สร้าง logging interceptor เพื่อแสดงข้อมูลการส่ง HTTP request/response
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // แสดงทั้ง headers และ body
        }

        // สร้างและคืนค่า OkHttpClient พร้อมการตั้งค่า
        return OkHttpClient.Builder()
            .addInterceptor(logging) // เพิ่ม interceptor เพื่อการ logging
            .build()
    }

    /**
     * สร้าง Retrofit สำหรับเรียกใช้ API
     *
     * Retrofit เป็นไลบรารีที่ช่วยให้เรียกใช้ REST API ได้ง่าย
     * โดยแปลง API endpoints เป็น Kotlin interfaces
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // URL หลักของ API
            .client(okHttpClient) // ใช้ OkHttpClient ที่สร้างจากเมธอดก่อนหน้า
            .addConverterFactory(GsonConverterFactory.create()) // ใช้ Gson ในการแปลง JSON
            .build()
    }

    /**
     * สร้าง Room Database ซึ่งเป็นฐานข้อมูลท้องถิ่นของแอพ
     *
     * Room ช่วยให้การจัดการฐานข้อมูล SQLite ง่ายขึ้น
     * และเชื่อมโยงกับ Architecture Components ได้ดี
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, // ต้องใช้ context ในการสร้าง Room database
            AppDatabase::class.java, // คลาส Database ที่เราสร้างขึ้น
            "app_database" // ชื่อไฟล์ฐานข้อมูล
        ).build()
    }
    //--DEFAULT ต้องเขียน--//



    //--เขียนตาม feature ที่มี ว่ามีอะไรบ้าง--//
    /**
     * สร้าง UserApi interface ที่ใช้เรียก API endpoints เกี่ยวกับ User
     *
     * Retrofit จะสร้าง implementation ของ interface นี้ให้อัตโนมัติ
     */
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }


    /**
     * สร้าง UserDao สำหรับการเข้าถึงข้อมูล User ในฐานข้อมูลท้องถิ่น
     *
     * DAO (Data Access Object) เป็นส่วนที่ใช้จัดการการเข้าถึงข้อมูล
     * เช่น การอ่าน เขียน แก้ไข หรือลบข้อมูลในฐานข้อมูล
     */
    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao() // เรียกใช้เมธอด userDao() จาก AppDatabase
    }

    /**
     * สร้าง UserRepository สำหรับจัดการข้อมูล User
     *
     * Repository เป็นส่วนที่ทำหน้าที่เป็นตัวกลางระหว่างแหล่งข้อมูลต่างๆ (API, Database)
     * กับส่วนอื่นๆ ของแอพ ตาม Clean Architecture
     */
    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi, userDao: UserDao): UserRepository {
        // สร้าง UserRepositoryImpl ที่รับ userApi และ userDao เป็น dependencies
        return UserRepositoryImpl(userApi, userDao)
    }
}