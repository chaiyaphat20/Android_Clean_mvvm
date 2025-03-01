package com.example.cleanarchitecturemvvmandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleanarchitecturemvvmandroid.data.local.dao.UserDao
import com.example.cleanarchitecturemvvmandroid.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}