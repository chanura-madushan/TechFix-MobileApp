package com.example.techfix.data;

import com.example.techfix.model.RepairService;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RepairServiceRepository {

    private final CollectionReference servicesRef;

    public RepairServiceRepository() {
        servicesRef = FirebaseFirestore.getInstance().collection("repairServices");
    }

    public void insertService(RepairService service) {
        servicesRef.add(service);
    }

    public void getServicesByCategory(String categoryId, FirestoreCallback<RepairService> callback) {
        servicesRef.whereEqualTo("categoryId", categoryId).get().addOnSuccessListener(querySnapshot -> {
            List<RepairService> services = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                RepairService service = doc.toObject(RepairService.class);
                service.serviceId = doc.getId();
                services.add(service);
            }
            callback.onSuccess(services);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getServiceById(String serviceId, FirestoreSingleCallback<RepairService> callback) {
        servicesRef.document(serviceId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                RepairService service = doc.toObject(RepairService.class);
                service.serviceId = doc.getId();
                callback.onSuccess(service);
            } else {
                callback.onFailure(new Exception("Service not found"));
            }
        }).addOnFailureListener(callback::onFailure);
    }
}