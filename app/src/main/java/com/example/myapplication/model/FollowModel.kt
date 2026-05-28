package com.example.myapplication.model

data class FollowModel (
    val userId : String,
    val username : String,
    val fullName : String,
    val photo : String?
)

data class FollowToggleResponse(
    val isFollowing: Boolean
)