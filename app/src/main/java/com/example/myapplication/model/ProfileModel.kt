package com.example.myapplication.model

data class Profile(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val password: String? = null,
    val dateOfBirth: String? = null,
    val joinDate: String? = null,
    val photo: String? = null,
    val posts: List<Post?> = emptyList(),
    val followersCount: Int = 0,
    val followingCount: Int = 0
)

data class UpdateProfileRequest(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val password: String? = null
)