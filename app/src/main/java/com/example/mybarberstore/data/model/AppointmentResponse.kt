package com.example.mybarberstore.data.model

data class AppointmentResponse(
    val id: Long,
    val date: String,            // Viene como "2026-05-07"
    val startTime: String,       // Viene como "17:00:00"
    val customerName: String,
    val customerId: Long,
    val telNumber: String,
    val serviceName: String,
    val serviceId: Long,
    val employeeName: String,
    val employeeId: Long,
    val durationMinutes: Int,
    val price: Double,
    val status: String           // "CONFIRMED", "PENDING", etc.
)