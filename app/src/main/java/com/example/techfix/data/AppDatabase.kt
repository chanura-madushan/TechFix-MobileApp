package com.example.techfix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.techfix.data.dao.BranchDao
import com.example.techfix.data.entity.Branch

@Database(
    entities = [Branch::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun branchDao(): BranchDao

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