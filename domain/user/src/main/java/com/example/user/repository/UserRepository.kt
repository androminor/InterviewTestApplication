package com.example.user.repository

import com.example.network.ResponseState
import com.example.network.model.User

interface UserRepository {
    suspend fun getUsers(): ResponseState<List<User>>
}