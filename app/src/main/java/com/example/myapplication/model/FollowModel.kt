package com.example.myapplication.model

data class FollowModel (
    val userId : String? = null,
    val username : String? = null,
    val fullName : String? = null,
    val photo : String?
)

data class FollowToggleResponse(
    val isFollowing: Boolean
)