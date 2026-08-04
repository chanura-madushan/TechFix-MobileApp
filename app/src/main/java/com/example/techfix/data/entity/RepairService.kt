package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "repair_services",
    foreignKeys = [
        ForeignKey(
            entity = DeviceCategory::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RepairService(
    @PrimaryKey(autoGenerate = true)
    val serviceId: Int = 0,
    val categoryId: Int,
    val name: String,       // e.g. "Screen Replacement"
    val description: String,
    val price: Double
)