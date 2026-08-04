package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val role: String, // "customer" or "admin"
    val phone: String = ""
)