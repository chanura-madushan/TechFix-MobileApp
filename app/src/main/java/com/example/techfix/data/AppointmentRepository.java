package com.example.techfix.data;

import com.example.techfix.model.Appointment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    private final CollectionReference appointmentsRef;

    public AppointmentRepository() {
        appointmentsRef = FirebaseFirestore.getInstance().collection("appointments");
    }

    public void insertAppointment(Appointment appointment, FirestoreSingleCallback<Appointment> callback) {
        appointmentsRef.add(appointment).addOnSuccessListener(docRef -> {
            appointment.appointmentId = docRef.getId();
            callback.onSuccess(appointment);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getAppointmentsByCustomer(String customerId, FirestoreCallback<Appointment> callback) {
        appointmentsRef.whereEqualTo("customerId", customerId).get().addOnSuccessListener(querySnapshot -> {
            List<Appointment> appointments = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                Appointment appointment = doc.toObject(Appointment.class);
                appointment.appointmentId = doc.getId();
                appointments.add(appointment);
            }
            callback.onSuccess(appointments);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getAppointmentById(String appointmentId, FirestoreSingleCallback<Appointment> callback) {
        appointmentsRef.document(appointmentId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                Appointment appointment = doc.toObject(Appointment.class);
                appointment.appointmentId = doc.getId();
                callback.onSuccess(appointment);
            } else {
                callback.onFailure(new Exception("Appointment not found"));
            }
        }).addOnFailureListener(callback::onFailure);
    }

    /** Returns every appointment that is not yet Completed, across all customers — used by the Admin Panel. */
    public void getActiveAppointments(FirestoreCallback<Appointment> callback) {
        appointmentsRef.get().addOnSuccessListener(querySnapshot -> {
            List<Appointment> appointments = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                Appointment appointment = doc.toObject(Appointment.class);
                appointment.appointmentId = doc.getId();
                if (!"Completed".equals(appointment.status)) {
                    appointments.add(appointment);
                }
            }
            callback.onSuccess(appointments);
        }).addOnFailureListener(callback::onFailure);
    }

    public void updateStatus(String appointmentId, String newStatus) {
        appointmentsRef.document(appointmentId).update("status", newStatus);
    }

    public void updateImage(String appointmentId, String imageUri) {
        appointmentsRef.document(appointmentId).update("imageUri", imageUri);
    }
}