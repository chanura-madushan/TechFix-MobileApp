package com.example.techfix.data;

import com.example.techfix.model.Branch;
import com.example.techfix.model.DeviceCategory;
import com.example.techfix.model.RepairService;

import java.util.List;

public class DataSeeder {

    public static void seedIfEmpty() {
        BranchRepository branchRepository = new BranchRepository();

        branchRepository.getAllBranches(new FirestoreCallback<Branch>() {
            @Override
            public void onSuccess(List<Branch> result) {
                if (!result.isEmpty()) return; // already seeded

                branchRepository.insertBranch(new Branch(
                        "Colombo Branch", "123 Galle Road, Colombo", 6.9271, 79.8612));
                branchRepository.insertBranch(new Branch(
                        "Galle Branch", "45 Lighthouse Street, Galle", 6.0535, 80.2210));

                DeviceCategoryRepository categoryRepository = new DeviceCategoryRepository();
                RepairServiceRepository serviceRepository = new RepairServiceRepository();

                DeviceCategory smartphone = new DeviceCategory("Smartphone");
                DeviceCategory laptop = new DeviceCategory("Laptop");
                DeviceCategory tablet = new DeviceCategory("Tablet");

                categoryRepository.insertCategory(smartphone);
                categoryRepository.insertCategory(laptop);
                categoryRepository.insertCategory(tablet);

                // Note: Firestore document IDs are only known after the write completes.
                // For seeding, we re-fetch categories once written so services can reference the real IDs.
                categoryRepository.getAllCategories(new FirestoreCallback<DeviceCategory>() {
                    @Override
                    public void onSuccess(List<DeviceCategory> categories) {
                        for (DeviceCategory c : categories) {
                            if (c.name.equals("Smartphone")) {
                                serviceRepository.insertService(new RepairService(c.categoryId, "Screen Replacement", "Replace cracked or unresponsive screen", 8500.0));
                                serviceRepository.insertService(new RepairService(c.categoryId, "Battery Replacement", "Replace degraded battery", 4500.0));
                            } else if (c.name.equals("Laptop")) {
                                serviceRepository.insertService(new RepairService(c.categoryId, "Keyboard Repair", "Fix unresponsive or damaged keys", 6000.0));
                                serviceRepository.insertService(new RepairService(c.categoryId, "Motherboard Diagnostic", "Diagnose hardware faults", 3000.0));
                            } else if (c.name.equals("Tablet")) {
                                serviceRepository.insertService(new RepairService(c.categoryId, "Screen Replacement", "Replace cracked tablet screen", 9500.0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });
            }

            @Override
            public void onFailure(Exception e) {}
        });
    }
}