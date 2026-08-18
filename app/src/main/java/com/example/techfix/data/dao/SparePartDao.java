package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.SparePart;
import java.util.List;
@Dao public interface SparePartDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) void insertPart(SparePart part);
 @Query("SELECT * FROM spare_parts WHERE branchId = :branchId") List<SparePart> getPartsByBranch(int branchId);
 @Query("SELECT * FROM spare_parts WHERE branchId = :branchId AND name = :name LIMIT 1") SparePart getPartByNameAndBranch(int branchId,String name);
 @Query("UPDATE spare_parts SET quantity = :newQuantity WHERE partId = :partId") void updateQuantity(int partId,int newQuantity);
}