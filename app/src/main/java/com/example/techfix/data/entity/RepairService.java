package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="repair_services", foreignKeys=@ForeignKey(entity=DeviceCategory.class,parentColumns="categoryId",childColumns="categoryId",onDelete=ForeignKey.CASCADE))
public class RepairService {
    @PrimaryKey(autoGenerate=true) private int serviceId;
    private int categoryId; private String name; private String description; private double price;
    public RepairService(int categoryId,String name,String description,double price){this.categoryId=categoryId;this.name=name;this.description=description;this.price=price;}
    public int getServiceId(){return serviceId;} public void setServiceId(int v){serviceId=v;}
    public int getCategoryId(){return categoryId;} public String getName(){return name;} public String getDescription(){return description;} public double getPrice(){return price;}
}