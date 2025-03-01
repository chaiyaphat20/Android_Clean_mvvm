package com.example.cleanarchitecturemvvmandroid.data.mapper

import com.example.cleanarchitecturemvvmandroid.data.remote.model.UserResponse
import com.example.cleanarchitecturemvvmandroid.domain.model.Address
import com.example.cleanarchitecturemvvmandroid.domain.model.Company
import com.example.cleanarchitecturemvvmandroid.domain.model.Geo
import com.example.cleanarchitecturemvvmandroid.domain.model.User

object UserMapper {
    fun UserResponse.toDomainModel(): User {
        return User(
            id = id,
            name = name,
            username = username,
            email = email,
            phone = phone,
            website = website,
            address = Address(
                street = address.street,
                suite = address.suite,
                city = address.city,
                zipcode = address.zipcode,
                geo = Geo(
                    lat = address.geo.lat,
                    lng = address.geo.lng
                )
            ),
            company = Company(
                name = company.name,
                catchPhrase = company.catchPhrase,
                bs = company.bs
            )
        )
    }
}