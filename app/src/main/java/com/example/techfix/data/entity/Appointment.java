package com.example.techfix.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="appointments", foreignKeys={
 @ForeignKey(entity=User.class,parentColumns="userId",childColumns="customerId",onDelete=ForeignKey.CASCADE),
 @ForeignKey(entity=Branch.class,parentColumns="branchId",childColumns="branchId",onDelete=ForeignKey.CASCADE),
 @ForeignKey(entity=RepairService.class,parentColumns="serviceId",childColumns="serviceId",onDelete=ForeignKey.CASCADE)})
public class Appointment {
 @PrimaryKey(autoGenerate=true) private int appointmentId; private int customerId; private int branchId; private int serviceId; private String requestedDate; private String status; private String imageUri; private String notes;
 public Appointment(int customerId,int branchId,int serviceId,String requestedDate,String status,String imageUri,String notes){this.customerId=customerId;this.branchId=branchId;this.serviceId=serviceId;this.requestedDate=requestedDate;this.status=status;this.imageUri=imageUri;this.notes=notes;}
 public Appointment(int customerId,int branchId,int serviceId,String requestedDate,String status){this(customerId,branchId,serviceId,requestedDate,status,null,"");}
 public int getAppointmentId(){return appointmentId;} public void setAppointmentId(int v){appointmentId=v;} public int getCustomerId(){return customerId;} public int getBranchId(){return branchId;} public int getServiceId(){return serviceId;} public String getRequestedDate(){return requestedDate;} public String getStatus(){return status;} public String getImageUri(){return imageUri;} public String getNotes(){return notes;}
}