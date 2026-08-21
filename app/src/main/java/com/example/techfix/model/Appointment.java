package com.example.techfix.model;

public class Appointment {
    public String appointmentId;
    public String customerId;
    public String branchId;
    public String serviceId;
    public String requestedDate;
    public String status;
    public String imageUri;
    public String notes;

    public Appointment() {}

    public Appointment(String customerId, String branchId, String serviceId, String requestedDate) {
        this.customerId = customerId;
        this.branchId = branchId;
        this.serviceId = serviceId;
        this.requestedDate = requestedDate;
        this.status = "Pending";
        this.notes = "";
    }
}