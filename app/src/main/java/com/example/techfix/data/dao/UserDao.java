package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.User;
@Dao public interface UserDao {
 @Insert(onConflict=OnConflictStrategy.ABORT) long insertUser(User user);
 @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1") User login(String email,String password);
 @Query("SELECT * FROM users WHERE email = :email LIMIT 1") User getUserByEmail(String email);
 @Query("SELECT * FROM users WHERE userId = :id") User getUserById(int id);
}