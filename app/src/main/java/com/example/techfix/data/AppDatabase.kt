package com.example.techfix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.techfix.data.dao.AppointmentDao
import com.example.techfix.data.dao.BranchDao
import com.example.techfix.data.dao.DeviceCategoryDao
import com.example.techfix.data.dao.PaymentDao
import com.example.techfix.data.dao.RepairServiceDao
import com.example.techfix.data.dao.SparePartDao
import com.example.techfix.data.dao.UserDao
import com.example.techfix.data.entity.Appointment
import com.example.techfix.data.entity.Branch
import com.example.techfix.data.entity.DeviceCategory
import com.example.techfix.data.entity.Payment
import com.example.techfix.data.entity.RepairService
import com.example.techfix.data.entity.SparePart
import com.example.techfix.data.entity.User

@Database(
    entities = [
        Branch::class,
        User::class,
        DeviceCategory::class,
        RepairService::class,
        SparePart::class,
        Appointment::class,
        Payment::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun branchDao(): BranchDao
    abstract fun userDao(): UserDao
    abstract fun deviceCategoryDao(): DeviceCategoryDao
    abstract fun repairServiceDao(): RepairServiceDao
    abstract fun sparePartDao(): SparePartDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "techfix_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}