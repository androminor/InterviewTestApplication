package com.example.network.model

import com.google.gson.annotations.SerializedName

data class NetworkUser(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: NetworkAddress
)

data class NetworkAddress(
    @SerializedName("city")
    val city: String
)


/*

data class NetworkUser(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: AddressDto
)

data class AddressDto(
@SerializedName("city")
    val city: String
)
*/
