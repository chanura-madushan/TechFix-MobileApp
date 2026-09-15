package com.example.techfix.ui;

public class AdminAppointmentDisplay {
    public String appointmentId;
    public String customerName;
    public String serviceName;
    public String branchName;
    public String date;
    public String status;

    public AdminAppointmentDisplay(String appointmentId, String customerName, String serviceName,
                                   String branchName, String date, String status) {
        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.branchName = branchName;
        this.date = date;
        this.status = status;
    }
}