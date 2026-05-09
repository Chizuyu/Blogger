package com.example.myapplication.model

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val thumbnail: String?,
    val imageContent: String?,
    val date: String,
    val likeCount: Int,

    val user: Author,
    val category: Category
)

data class LikedRequest(
    val postId: String
)
data class Author(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val username: String? = null,
    val dateOfBirth: String? = null,
    val joinDate: String? = null,
    val photo: String? = null
)

data class Category(
    val id: String? = null,
    val name: String
)