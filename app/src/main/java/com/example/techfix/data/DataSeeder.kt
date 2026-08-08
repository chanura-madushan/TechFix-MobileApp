package com.example.techfix.data

import com.example.techfix.data.entity.Branch
import com.example.techfix.data.entity.DeviceCategory
import com.example.techfix.data.entity.RepairService

object DataSeeder {

    suspend fun seedIfEmpty(db: AppDatabase) {
        val branchDao = db.branchDao()
        val categoryDao = db.deviceCategoryDao()
        val serviceDao = db.repairServiceDao()

        if (branchDao.getAllBranches().isNotEmpty()) return // already seeded

        branchDao.insertBranch(
            Branch(name = "Colombo Branch", address = "123 Galle Road, Colombo", latitude = 6.9271, longitude = 79.8612)
        )
        branchDao.insertBranch(
            Branch(name = "Galle Branch", address = "45 Lighthouse Street, Galle", latitude = 6.0535, longitude = 80.2210)
        )

        categoryDao.insertCategory(DeviceCategory(categoryId = 1, name = "Smartphone"))
        categoryDao.insertCategory(DeviceCategory(categoryId = 2, name = "Laptop"))
        categoryDao.insertCategory(DeviceCategory(categoryId = 3, name = "Tablet"))

        serviceDao.insertService(RepairService(categoryId = 1, name = "Screen Replacement", description = "Replace cracked or unresponsive screen", price = 8500.0))
        serviceDao.insertService(RepairService(categoryId = 1, name = "Battery Replacement", description = "Replace degraded battery", price = 4500.0))
        serviceDao.insertService(RepairService(categoryId = 2, name = "Keyboard Repair", description = "Fix unresponsive or damaged keys", price = 6000.0))
        serviceDao.insertService(RepairService(categoryId = 2, name = "Motherboard Diagnostic", description = "Diagnose hardware faults", price = 3000.0))
        serviceDao.insertService(RepairService(categoryId = 3, name = "Screen Replacement", description = "Replace cracked tablet screen", price = 9500.0))
    }
}