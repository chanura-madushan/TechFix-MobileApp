package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.Payment

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long

    @Query("SELECT * FROM payments WHERE appointmentId = :appointmentId LIMIT 1")
    suspend fun getPaymentByAppointment(appointmentId: Int): Payment?

    @Query("UPDATE payments SET status = :newStatus, paymentDate = :date WHERE paymentId = :id")
    suspend fun markAsPaid(id: Int, newStatus: String, date: String)
}