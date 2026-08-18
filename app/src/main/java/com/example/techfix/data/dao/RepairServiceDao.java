package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.RepairService;
import java.util.List;
@Dao public interface RepairServiceDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) void insertService(RepairService service);
 @Query("SELECT * FROM repair_services") List<RepairService> getAllServices();
 @Query("SELECT * FROM repair_services WHERE categoryId = :categoryId") List<RepairService> getServicesByCategory(int categoryId);
 @Query("SELECT * FROM repair_services WHERE serviceId = :id") RepairService getServiceById(int id);
}