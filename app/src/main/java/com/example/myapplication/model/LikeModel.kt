package com.example.myapplication.model

data class LikeRequest(
    val postId: String
)

data class PostRequest(
    val title: String,
    val content: String,
    val categoryId: String
)