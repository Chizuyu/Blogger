package com.example.myapplication.model

data class Comment(
    val id: String,
    val content: String,
    val createdAt: String,
    val user: Author // Nested author
)

data class CommentRequest(
    val content: String
)