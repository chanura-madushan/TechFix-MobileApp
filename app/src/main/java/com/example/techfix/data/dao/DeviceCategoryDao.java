package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.DeviceCategory;
import java.util.List;
@Dao public interface DeviceCategoryDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) void insertCategory(DeviceCategory category);
 @Query("SELECT * FROM device_categories") List<DeviceCategory> getAllCategories();
}