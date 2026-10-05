package com.example.post

import com.example.network.ApiService
import com.example.network.ResponseState
import com.example.network.safeApiCall
import javax.inject.Inject

/*
class PostRepositoryImpl @Inject constructor(private val apiService: ApiService) : PostRepository {
    override suspend fun getPostsByUser(userId: Int): ResponseState<List<Post>> {
        val result = sa { apiService.getPostsByUser(userId) }
        return when (result) {
            is ResponseState.Success ->{
                val posts = result.data?.map { it.toDomain() } ?: emptyList()
                ResponseState.Success(posts)
            }
            is ResponseState.Error -> ResponseState.Error(result.error)
        }
    }
}*/
