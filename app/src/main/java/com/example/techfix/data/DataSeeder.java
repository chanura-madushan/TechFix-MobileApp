package com.example.techfix.data;
import com.example.techfix.data.entity.*;
public final class DataSeeder {
 private DataSeeder(){}
 public static void seedIfEmpty(AppDatabase db){
  if(!db.branchDao().getAllBranches().isEmpty()) return;
  db.branchDao().insertBranch(new Branch("Colombo Branch","123 Galle Road, Colombo",6.9271,79.8612));
  db.branchDao().insertBranch(new Branch("Galle Branch","45 Lighthouse Street, Galle",6.0535,80.2210));
  db.deviceCategoryDao().insertCategory(new DeviceCategory(1,"Smartphone"));
  db.deviceCategoryDao().insertCategory(new DeviceCategory(2,"Laptop"));
  db.deviceCategoryDao().insertCategory(new DeviceCategory(3,"Tablet"));
  db.repairServiceDao().insertService(new RepairService(1,"Screen Replacement","Replace cracked or unresponsive screen",8500.0));
  db.repairServiceDao().insertService(new RepairService(1,"Battery Replacement","Replace degraded battery",4500.0));
  db.repairServiceDao().insertService(new RepairService(2,"Keyboard Repair","Fix unresponsive or damaged keys",6000.0));
  db.repairServiceDao().insertService(new RepairService(2,"Motherboard Diagnostic","Diagnose hardware faults",3000.0));
  db.repairServiceDao().insertService(new RepairService(3,"Screen Replacement","Replace cracked tablet screen",9500.0));
 }
}