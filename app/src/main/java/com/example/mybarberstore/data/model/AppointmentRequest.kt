package com.example.mybarberstore.data.model

data class AppointmentRequest(
    val serviceId: Long,
    val employeeId: Long,
    val clientId: Long,
    val status: String,
    val date: String, // Formato "yyyy-MM-dd"
    val startTime: String  // Formato "HH:mm"
)


