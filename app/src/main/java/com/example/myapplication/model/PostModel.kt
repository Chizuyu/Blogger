package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

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

data class CreatePostRequest(
    val id: String,
    val categoryId: String,
    val title: String,
    val content: String
)

data class LikeCountResponse(
    val count: Int
)

data class IsLikedResponse(
    val isLiked: Boolean
)