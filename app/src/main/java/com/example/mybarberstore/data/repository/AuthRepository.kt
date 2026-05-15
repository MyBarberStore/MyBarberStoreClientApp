package com.example.mybarberstore.data.repository

import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.data.model.LoginRequest
import com.example.mybarberstore.data.model.LoginResponse
import com.example.mybarberstore.data.model.RegisterRequest

import retrofit2.Response

class AuthRepository {
    private val apiService = RetroFitClient.instance

    suspend fun login(email: String, pass: String): Response<LoginResponse>? {
        return try {
            val request = LoginRequest(email, pass)
            apiService.login(request)
        } catch (e: Exception) {
            null // Si hay error de conexión (servidor apagado, sin internet)
        }
    }

    suspend fun register(request: RegisterRequest): Response<Void>? {
        return try {
            apiService.register(request)
        } catch (e: Exception) {
            null
        }
    }
}