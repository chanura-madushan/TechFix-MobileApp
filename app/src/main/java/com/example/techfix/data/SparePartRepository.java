package com.example.techfix.data;

import com.example.techfix.model.SparePart;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SparePartRepository {

    private final CollectionReference partsRef;

    public SparePartRepository() {
        partsRef = FirebaseFirestore.getInstance().collection("spareParts");
    }

    public void insertPart(SparePart part) {
        partsRef.add(part);
    }

    public void getPartsByBranch(String branchId, FirestoreCallback<SparePart> callback) {
        partsRef.whereEqualTo("branchId", branchId).get().addOnSuccessListener(querySnapshot -> {
            List<SparePart> parts = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                SparePart part = doc.toObject(SparePart.class);
                part.partId = doc.getId();
                parts.add(part);
            }
            callback.onSuccess(parts);
        }).addOnFailureListener(callback::onFailure);
    }

    public void updateQuantity(String partId, int newQuantity) {
        partsRef.document(partId).update("quantity", newQuantity);
    }
}