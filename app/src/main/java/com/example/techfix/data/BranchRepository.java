package com.example.techfix.data;

import com.example.techfix.model.Branch;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.techfix.data.FirestoreSingleCallback;
import java.util.ArrayList;
import java.util.List;

public class BranchRepository {

    private final CollectionReference branchesRef;

    public BranchRepository() {
        branchesRef = FirebaseFirestore.getInstance().collection("branches");
    }

    public void insertBranch(Branch branch) {
        branchesRef.add(branch);
    }

    public void getAllBranches(FirestoreCallback<Branch> callback) {
        branchesRef.get().addOnSuccessListener(querySnapshot -> {
            List<Branch> branches = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                Branch branch = doc.toObject(Branch.class);
                branch.branchId = doc.getId();
                branches.add(branch);
            }
            callback.onSuccess(branches);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getBranchById(String branchId, FirestoreSingleCallback<Branch> callback) {
        branchesRef.document(branchId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                Branch branch = doc.toObject(Branch.class);
                branch.branchId = doc.getId();
                callback.onSuccess(branch);
            } else {
                callback.onFailure(new Exception("Branch not found"));
            }
        }).addOnFailureListener(callback::onFailure);
    }
}