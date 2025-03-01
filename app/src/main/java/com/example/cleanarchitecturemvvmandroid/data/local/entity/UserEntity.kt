package com.example.cleanarchitecturemvvmandroid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,

    // Address fields (flattened for Room)
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val lat: String,
    val lng: String,

    // Company fields (flattened for Room)
    val companyName: String,
    val companyCatchPhrase: String,
    val companyBs: String
)