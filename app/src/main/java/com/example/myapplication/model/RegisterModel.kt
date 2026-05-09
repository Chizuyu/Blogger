package com.example.myapplication.model

data class Register (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val password: String = "",
    val cPassword: String = "",
    val dateOfBirth: String = "",
    val joinDate: String = "",
    val photo: String = ""
)

data class RegisterResponse(
    val token: String?,
    val message: String?
)