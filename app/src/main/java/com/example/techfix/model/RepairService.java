package com.example.techfix.model;

public class RepairService {
    public String serviceId;
    public String categoryId;
    public String name;
    public String description;
    public double price;

    public RepairService() {}

    public RepairService(String categoryId, String name, String description, double price) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}