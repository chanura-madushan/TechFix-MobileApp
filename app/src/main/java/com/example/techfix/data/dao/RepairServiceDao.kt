package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.RepairService

@Dao
interface RepairServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: RepairService)

    @Query("SELECT * FROM repair_services")
    suspend fun getAllServices(): List<RepairService>

    @Query("SELECT * FROM repair_services WHERE categoryId = :categoryId")
    suspend fun getServicesByCategory(categoryId: Int): List<RepairService>

    @Query("SELECT * FROM repair_services WHERE serviceId = :id")
    suspend fun getServiceById(id: Int): RepairService?
}