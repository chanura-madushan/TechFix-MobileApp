package com.example.techfix.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.techfix.data.dao.*;
import com.example.techfix.data.entity.*;

@Database(entities={Branch.class,User.class,DeviceCategory.class,RepairService.class,SparePart.class,Appointment.class,Payment.class},version=1,exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
 public abstract BranchDao branchDao(); public abstract UserDao userDao(); public abstract DeviceCategoryDao deviceCategoryDao(); public abstract RepairServiceDao repairServiceDao(); public abstract SparePartDao sparePartDao(); public abstract AppointmentDao appointmentDao(); public abstract PaymentDao paymentDao();
 private static volatile AppDatabase INSTANCE;
 public static AppDatabase getDatabase(Context context){
  if(INSTANCE==null){ synchronized(AppDatabase.class){ if(INSTANCE==null) INSTANCE=Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"techfix_database").build(); } }
  return INSTANCE;
 }
}