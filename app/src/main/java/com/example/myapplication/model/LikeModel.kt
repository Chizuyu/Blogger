package com.example.myapplication.model

// Untuk kirim Like
data class LikeRequest(
    val postId: String
)

// Untuk buat Post baru
data class PostRequest(
    val title: String,
    val content: String,
    val categoryId: String
)