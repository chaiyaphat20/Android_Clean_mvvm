package com.example.cleanarchitecturemvvmandroid.data.remote.model

import com.example.cleanarchitecturemvvmandroid.domain.model.Address
import com.example.cleanarchitecturemvvmandroid.domain.model.Company
import com.example.cleanarchitecturemvvmandroid.domain.model.Geo
import com.example.cleanarchitecturemvvmandroid.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("website") val website: String,
    @SerializedName("address") val address: AddressResponse,
    @SerializedName("company") val company: CompanyResponse
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

data class AddressResponse(
    @SerializedName("street") val street: String,
    @SerializedName("suite") val suite: String,
    @SerializedName("city") val city: String,
    @SerializedName("zipcode") val zipcode: String,
    @SerializedName("geo") val geo: GeoResponse
)

data class GeoResponse(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
)

data class CompanyResponse(
    @SerializedName("name") val name: String,
    @SerializedName("catchPhrase") val catchPhrase: String,
    @SerializedName("bs") val bs: String
)
