package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.Branch;
import java.util.List;
@Dao public interface BranchDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) void insertBranch(Branch branch);
 @Query("SELECT * FROM branches") List<Branch> getAllBranches();
 @Query("SELECT * FROM branches WHERE branchId = :id") Branch getBranchById(int id);
}