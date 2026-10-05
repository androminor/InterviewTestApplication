package com.example.network.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id")
    val id:String,
    @SerializedName("userId")
    val userId:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("body")
    val body:String
)
