package com.example.techfix.ui

data class AppointmentDisplay(
    val appointmentId: Int,
    val date: String,
    val branchName: String,
    val serviceName: String,
    val status: String
)