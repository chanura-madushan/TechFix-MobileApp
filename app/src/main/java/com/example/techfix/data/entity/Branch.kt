package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "branches")
data class Branch(
    @PrimaryKey(autoGenerate = true)
    val branchId: Int = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)