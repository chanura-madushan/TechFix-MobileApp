package com.example.techfix.data;

import com.example.techfix.model.User;
import com.example.techfix.util.PasswordUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {

    private final CollectionReference usersRef;

    public UserRepository() {
        usersRef = FirebaseFirestore.getInstance().collection("users");
    }

    public void register(String name, String email, String password, String phone, FirestoreSingleCallback<User> callback) {
        usersRef.whereEqualTo("email", email).get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                callback.onFailure(new Exception("An account with this email already exists"));
                return;
            }
            String hashed = PasswordUtils.hash(password);
            User newUser = new User(name, email, hashed, "customer", phone);
            usersRef.add(newUser).addOnSuccessListener(docRef -> {
                newUser.userId = docRef.getId();
                callback.onSuccess(newUser);
            }).addOnFailureListener(callback::onFailure);
        }).addOnFailureListener(callback::onFailure);
    }

    public void login(String email, String password, FirestoreSingleCallback<User> callback) {
        usersRef.whereEqualTo("email", email).get().addOnSuccessListener(querySnapshot -> {
            if (querySnapshot.isEmpty()) {
                callback.onFailure(new Exception("No account found with this email"));
                return;
            }
            QueryDocumentSnapshot doc = querySnapshot.getDocuments().get(0) instanceof QueryDocumentSnapshot
                    ? (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0) : null;
            User user = querySnapshot.getDocuments().get(0).toObject(User.class);
            user.userId = querySnapshot.getDocuments().get(0).getId();

            if (PasswordUtils.verify(password, user.password)) {
                callback.onSuccess(user);
            } else {
                callback.onFailure(new Exception("Incorrect password"));
            }
        }).addOnFailureListener(callback::onFailure);
    }

    public void getUserById(String userId, FirestoreSingleCallback<User> callback) {
        usersRef.document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                User user = doc.toObject(User.class);
                user.userId = doc.getId();
                callback.onSuccess(user);
            } else {
                callback.onFailure(new Exception("User not found"));
            }
        }).addOnFailureListener(callback::onFailure);
    }
}