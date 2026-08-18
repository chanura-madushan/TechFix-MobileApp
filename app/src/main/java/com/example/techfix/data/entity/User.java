package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;

    public User(String name, String email, String password, String role, String phone) {
        this.name = name; this.email = email; this.password = password; this.role = role; this.phone = phone;
    }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getPhone() { return phone; }
}