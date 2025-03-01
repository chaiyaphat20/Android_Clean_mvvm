package com.example.cleanarchitecturemvvmandroid.domain.model

import com.example.cleanarchitecturemvvmandroid.data.local.entity.UserEntity

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val address: Address,
    val company: Company

){
    fun toUserEntity(): UserEntity {
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
            companyBs = company.bs,
        )
    }
}

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
)

data class Geo(
    val lat: String,
    val lng: String
)

data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)