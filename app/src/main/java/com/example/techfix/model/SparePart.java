package com.example.techfix.model;

public class SparePart {
    public String partId;
    public String branchId;
    public String name;
    public int quantity;

    public SparePart() {}

    public SparePart(String branchId, String name, int quantity) {
        this.branchId = branchId;
        this.name = name;
        this.quantity = quantity;
    }
}