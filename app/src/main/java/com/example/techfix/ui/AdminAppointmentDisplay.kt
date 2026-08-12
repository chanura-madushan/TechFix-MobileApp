package com.example.techfix.ui

data class AdminAppointmentDisplay(
    val appointmentId: Int,
    val customerName: String,
    val serviceName: String,
    val branchName: String,
    val date: String,
    val status: String
)