package com.example.techfix.ui;

public class AppointmentDisplay {
    public String appointmentId;
    public String date;
    public String branchName;
    public String serviceName;
    public String status;

    public AppointmentDisplay(String appointmentId, String date, String branchName, String serviceName, String status) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.branchName = branchName;
        this.serviceName = serviceName;
        this.status = status;
    }
}