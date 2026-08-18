package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_categories")
public class DeviceCategory {
    @PrimaryKey(autoGenerate = true) private int categoryId;
    private String name;
    public DeviceCategory(String name){this.name=name;}
    public DeviceCategory(int categoryId,String name){this.categoryId=categoryId;this.name=name;}
    public int getCategoryId(){return categoryId;} public void setCategoryId(int v){categoryId=v;}
    public String getName(){return name;}
}