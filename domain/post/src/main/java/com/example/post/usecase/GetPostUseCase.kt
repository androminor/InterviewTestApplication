package com.example.post.usecase

import com.example.network.ResponseState
import com.example.post.Post
import com.example.post.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val postRepo: PostRepository) {
    suspend operator fun invoke(userId: Int): ResponseState<List<Post>> {
        return postRepo.getPostsByUser(userId)
    }
}
