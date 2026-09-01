package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "branches")
public class Branch {
    @PrimaryKey(autoGenerate = true) private int branchId;
    private String name; private String address; private double latitude; private double longitude;
    public Branch(String name, String address, double latitude, double longitude) { this.name=name; this.address=address; this.latitude=latitude; this.longitude=longitude; }
    public int getBranchId(){return branchId;} public void setBranchId(int v){branchId=v;}
    public String getName(){return name;} public String getAddress(){return address;} public double getLatitude(){return latitude;} public double getLongitude(){return longitude;}
}