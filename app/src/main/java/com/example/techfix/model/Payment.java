package com.example.techfix.model;

public class Payment {
    public String paymentId;
    public String appointmentId;
    public double amount;
    public String status;
    public String paymentDate;
    public String method;

    public Payment() {}

    public Payment(String appointmentId, double amount, String status, String paymentDate, String method) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.method = method;
    }
}