package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.Appointment;
import java.util.List;
@Dao public interface AppointmentDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) long insertAppointment(Appointment appointment);
 @Query("SELECT * FROM appointments WHERE customerId = :customerId ORDER BY requestedDate DESC") List<Appointment> getAppointmentsByCustomer(int customerId);
 @Query("SELECT * FROM appointments WHERE appointmentId = :id") Appointment getAppointmentById(int id);
 @Query("SELECT * FROM appointments WHERE branchId = :branchId ORDER BY requestedDate DESC") List<Appointment> getAppointmentsByBranch(int branchId);
 @Query("SELECT * FROM appointments WHERE status != 'Completed' ORDER BY requestedDate ASC") List<Appointment> getActiveAppointments();
 @Query("UPDATE appointments SET status = :newStatus WHERE appointmentId = :id") void updateStatus(int id,String newStatus);
 @Query("UPDATE appointments SET imageUri = :uri WHERE appointmentId = :id") void updateImage(int id,String uri);
}