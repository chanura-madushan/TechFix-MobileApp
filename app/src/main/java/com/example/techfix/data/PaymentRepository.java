package com.example.techfix.data;

import com.example.techfix.model.Payment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentRepository {

    private final CollectionReference paymentsRef;

    public PaymentRepository() {
        paymentsRef = FirebaseFirestore.getInstance().collection("payments");
    }

    public void insertPayment(Payment payment) {
        paymentsRef.add(payment);
    }

    public void getPaymentByAppointment(String appointmentId, FirestoreSingleCallback<Payment> callback) {
        paymentsRef.whereEqualTo("appointmentId", appointmentId).get().addOnSuccessListener(querySnapshot -> {
            if (querySnapshot.isEmpty()) {
                callback.onFailure(new Exception("No payment found"));
                return;
            }
            Payment payment = querySnapshot.getDocuments().get(0).toObject(Payment.class);
            payment.paymentId = querySnapshot.getDocuments().get(0).getId();
            callback.onSuccess(payment);
        }).addOnFailureListener(callback::onFailure);
    }

    public void markAsPaid(String paymentId, String newStatus, String date) {
        paymentsRef.document(paymentId)
                .update("status", newStatus, "paymentDate", date);
    }
}