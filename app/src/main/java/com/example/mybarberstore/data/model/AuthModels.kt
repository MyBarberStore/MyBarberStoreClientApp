package com.example.mybarberstore.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que el servidor te devuelve (ajústalo a tu JSON de Spring)
data class LoginResponse(
    val token: String,
    val user: UserResponse
)
