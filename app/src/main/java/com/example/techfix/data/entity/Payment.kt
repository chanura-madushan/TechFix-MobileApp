package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = Appointment::class,
            parentColumns = ["appointmentId"],
            childColumns = ["appointmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int = 0,
    val appointmentId: Int,
    val amount: Double,
    val status: String = "Pending", // "Pending" -> "Paid"
    val paymentDate: String? = null,
    val method: String = "Cash"     // "Cash", "Card", etc.
)