package com.example.techfix.data;

import com.example.techfix.model.DeviceCategory;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeviceCategoryRepository {

    private final CollectionReference categoriesRef;

    public DeviceCategoryRepository() {
        categoriesRef = FirebaseFirestore.getInstance().collection("deviceCategories");
    }

    public void insertCategory(DeviceCategory category) {
        categoriesRef.add(category);
    }

    public void getAllCategories(FirestoreCallback<DeviceCategory> callback) {
        categoriesRef.get().addOnSuccessListener(querySnapshot -> {
            List<DeviceCategory> categories = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                DeviceCategory category = doc.toObject(DeviceCategory.class);
                category.categoryId = doc.getId();
                categories.add(category);
            }
            callback.onSuccess(categories);
        }).addOnFailureListener(callback::onFailure);
    }
}