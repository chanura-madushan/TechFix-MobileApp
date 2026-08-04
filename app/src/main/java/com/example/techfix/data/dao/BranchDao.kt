package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.Branch

@Dao
interface BranchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranch(branch: Branch)

    @Query("SELECT * FROM branches")
    suspend fun getAllBranches(): List<Branch>

    @Query("SELECT * FROM branches WHERE branchId = :id")
    suspend fun getBranchById(id: Int): Branch?
}