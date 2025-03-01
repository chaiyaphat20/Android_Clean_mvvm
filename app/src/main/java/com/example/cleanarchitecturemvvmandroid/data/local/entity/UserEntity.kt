package com.example.cleanarchitecturemvvmandroid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleanarchitecturemvvmandroid.domain.model.Address
import com.example.cleanarchitecturemvvmandroid.domain.model.Company
import com.example.cleanarchitecturemvvmandroid.domain.model.Geo
import com.example.cleanarchitecturemvvmandroid.domain.model.User

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
){
    fun toUser(): User {
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
                geo = Geo(lat = lat, lng = lng)
            ),
            company = Company(
                name = companyName,
                catchPhrase = companyCatchPhrase,
                bs = companyBs
            )
        )
    }
}