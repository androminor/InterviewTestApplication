package com.example.post

import com.example.network.model.PostDto


//Extension function
fun PostDto.toDomain():Post{
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}