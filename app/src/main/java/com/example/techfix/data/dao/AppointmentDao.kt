package com.example.techfix.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techfix.data.entity.Appointment

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment): Long

    @Query("SELECT * FROM appointments WHERE customerId = :customerId ORDER BY requestedDate DESC")
    suspend fun getAppointmentsByCustomer(customerId: Int): List<Appointment>

    @Query("SELECT * FROM appointments WHERE appointmentId = :id")
    suspend fun getAppointmentById(id: Int): Appointment?

    @Query("SELECT * FROM appointments WHERE branchId = :branchId ORDER BY requestedDate DESC")
    suspend fun getAppointmentsByBranch(branchId: Int): List<Appointment>

    @Query("SELECT * FROM appointments WHERE status != 'Completed' ORDER BY requestedDate ASC")
    suspend fun getActiveAppointments(): List<Appointment>

    @Query("UPDATE appointments SET status = :newStatus WHERE appointmentId = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

    @Query("UPDATE appointments SET imageUri = :uri WHERE appointmentId = :id")
    suspend fun updateImage(id: Int, uri: String)
}