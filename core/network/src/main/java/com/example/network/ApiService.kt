package com.example.network

import com.example.network.model.NetworkUser
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUser(): List<NetworkUser>
}