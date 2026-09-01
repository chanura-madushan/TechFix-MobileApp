package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="payments", foreignKeys=@ForeignKey(entity=Appointment.class,parentColumns="appointmentId",childColumns="appointmentId",onDelete=ForeignKey.CASCADE))
public class Payment {
 @PrimaryKey(autoGenerate=true) private int paymentId; private int appointmentId; private double amount; private String status; private String paymentDate; private String method;
 public Payment(int appointmentId,double amount,String status,String paymentDate,String method){this.appointmentId=appointmentId;this.amount=amount;this.status=status;this.paymentDate=paymentDate;this.method=method;}
 public int getPaymentId(){return paymentId;} public void setPaymentId(int v){paymentId=v;} public int getAppointmentId(){return appointmentId;} public double getAmount(){return amount;} public String getStatus(){return status;} public String getPaymentDate(){return paymentDate;} public String getMethod(){return method;}
}