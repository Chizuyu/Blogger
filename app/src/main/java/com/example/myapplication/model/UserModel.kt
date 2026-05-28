package com.example.myapplication.model

data class User(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val password: String? = null,
    val dateOfBirth: String? = null,
    val joinDate: String? = null,
    val photo: String? = null,
    val isFollowing: Boolean = false
)

data class LoginResponse(
    val token: String,
    val user: User
)