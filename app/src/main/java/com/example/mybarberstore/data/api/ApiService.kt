package com.example.mybarberstore.data.api

import com.example.mybarberstore.data.model.AppointmentRequest
import com.example.mybarberstore.data.model.AppointmentResponse
import com.example.mybarberstore.data.model.Barber
import com.example.mybarberstore.data.model.LoginRequest
import com.example.mybarberstore.data.model.LoginResponse
import com.example.mybarberstore.data.model.RegisterRequest
import com.example.mybarberstore.data.model.Service
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void> // Usamos Void si el backend solo devuelve un 201 Ok

    @GET("appointments/services") // Asegúrate de que esta ruta coincida con tu backend
    suspend fun getServices(): Response<List<Service>>

    @GET("employees")
    suspend fun getBarbers(): Response<List<Barber>>

    @GET("appointments/availability")
    suspend fun getAvailableHours(
        @Query("employeeId") barberId: Long,
        @Query("date") date: String
    ): Response<List<String>>

    @POST("appointments")
    suspend fun createAppointment(
        @Body request: AppointmentRequest
    ): Response<Void>

    @GET("appointments/user/{id}")
    suspend fun getClientHistory(@Path("id") id: Long): Response<List<AppointmentResponse>>

    @PUT("appointments/cancel/{id}")
    suspend fun cancelAppointment(@Path("id") id: Long): Response<Void>

    @Streaming
    @GET("billing/from-appointment/{id}/export-pdf")
    suspend fun downloadInvoice(@Path("id") id: Long): Response<ResponseBody>
}
