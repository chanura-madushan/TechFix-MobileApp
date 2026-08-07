package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Branch::class,
            parentColumns = ["branchId"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RepairService::class,
            parentColumns = ["serviceId"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val appointmentId: Int = 0,
    val customerId: Int,
    val branchId: Int,
    val serviceId: Int,
    val requestedDate: String,      // store as "yyyy-MM-dd" for simplicity
    val status: String = "Pending", // "Pending" -> "In Progress" -> "Completed"
    val imageUri: String? = null,   // photo of repaired device, added later
    val notes: String = ""
)