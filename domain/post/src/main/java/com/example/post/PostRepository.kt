package com.example.post

import com.example.network.ResponseState

interface PostRepository {
    suspend fun getPostsByUser(userId:Int): ResponseState<List<Post>>
}