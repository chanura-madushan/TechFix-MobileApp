package com.example.techfix.data.dao;
import androidx.room.*;
import com.example.techfix.data.entity.Payment;
@Dao public interface PaymentDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) long insertPayment(Payment payment);
 @Query("SELECT * FROM payments WHERE appointmentId = :appointmentId LIMIT 1") Payment getPaymentByAppointment(int appointmentId);
 @Query("UPDATE payments SET status = :newStatus, paymentDate = :date WHERE paymentId = :id") void markAsPaid(int id,String newStatus,String date);
}