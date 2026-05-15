package com.example.mybarberstore.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val telNumber: String,
    val role: String = "CUSTOMER"
)
