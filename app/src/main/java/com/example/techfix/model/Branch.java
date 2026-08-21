package com.example.techfix.model;

public class Branch {
    public String branchId;
    public String name;
    public String address;
    public double latitude;
    public double longitude;

    public Branch() {} // required empty constructor for Firestore

    public Branch(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}