package com.example.user

import com.example.network.ApiService
import com.example.network.ResponseState
import com.example.network.model.User
import com.example.network.safeApiCall
import com.example.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUsers(): ResponseState<List<User>> {
        val result = safeApiCall {
            apiService.getUser()
        }
        return when (result) {
            is ResponseState.Success -> {
                val users = result.data?.map { it.toDomain() } ?: emptyList()
                ResponseState.Success(users)
            }

            is ResponseState.Error -> {
                ResponseState.Error(result.error)
            }
        }

    }
}


