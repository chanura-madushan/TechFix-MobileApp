package com.example.techfix.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "spare_parts",
    foreignKeys = [
        ForeignKey(
            entity = Branch::class,
            parentColumns = ["branchId"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SparePart(
    @PrimaryKey(autoGenerate = true)
    val partId: Int = 0,
    val branchId: Int,
    val name: String,
    val quantity: Int
)