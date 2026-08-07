package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.SparePart

@Dao
interface SparePartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(part: SparePart)

    @Query("SELECT * FROM spare_parts WHERE branchId = :branchId")
    suspend fun getPartsByBranch(branchId: Int): List<SparePart>

    @Query("SELECT * FROM spare_parts WHERE branchId = :branchId AND name = :name LIMIT 1")
    suspend fun getPartByNameAndBranch(branchId: Int, name: String): SparePart?

    @Query("UPDATE spare_parts SET quantity = :newQuantity WHERE partId = :partId")
    suspend fun updateQuantity(partId: Int, newQuantity: Int)
}