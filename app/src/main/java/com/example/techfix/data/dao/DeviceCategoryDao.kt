package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.DeviceCategory

@Dao
interface DeviceCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: DeviceCategory)

    @Query("SELECT * FROM device_categories")
    suspend fun getAllCategories(): List<DeviceCategory>
}