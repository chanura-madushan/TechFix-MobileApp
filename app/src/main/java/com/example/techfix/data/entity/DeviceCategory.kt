package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_categories")
data class DeviceCategory(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,
    val name: String // e.g. "Laptop", "Smartphone", "Tablet"
)