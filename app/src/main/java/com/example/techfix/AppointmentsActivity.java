package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.AppointmentRepository;
import com.example.techfix.data.BranchRepository;
import com.example.techfix.data.FirestoreCallback;
import com.example.techfix.data.RepairServiceRepository;
import com.example.techfix.model.Appointment;
import com.example.techfix.model.Branch;
import com.example.techfix.model.RepairService;
import com.example.techfix.ui.AppointmentAdapter;
import com.example.techfix.ui.AppointmentDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        String userId = getIntent().getStringExtra("USER_ID");
        boolean filterCompleted = getIntent().getBooleanExtra("FILTER_COMPLETED", false);

        ((TextView) findViewById(R.id.tvAppointmentsTitle)).setText(filterCompleted ? "Repair History" : "My Appointments");

        RecyclerView recyclerView = findViewById(R.id.rvAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyState = findViewById(R.id.tvEmptyState);

        AppointmentRepository appointmentRepository = new AppointmentRepository();
        BranchRepository branchRepository = new BranchRepository();
        RepairServiceRepository serviceRepository = new RepairServiceRepository();

        appointmentRepository.getAppointmentsByCustomer(userId, new FirestoreCallback<Appointment>() {
            @Override
            public void onSuccess(List<Appointment> allAppointments) {
                List<Appointment> filtered = new ArrayList<>();
                for (Appointment a : allAppointments) {
                    if (!filterCompleted || "Completed".equals(a.status)) {
                        filtered.add(a);
                    }
                }

                if (filtered.isEmpty()) {
                    emptyState.setVisibility(View.VISIBLE);
                    emptyState.setText(filterCompleted ? "No completed repairs yet." : "No appointments yet.");
                    return;
                }
                emptyState.setVisibility(View.GONE);

                // Fetch branch and service reference data once, then join locally.
                branchRepository.getAllBranches(new FirestoreCallback<Branch>() {
                    @Override
                    public void onSuccess(List<Branch> branches) {
                        Map<String, String> branchNames = new HashMap<>();
                        for (Branch b : branches) branchNames.put(b.branchId, b.name);

                        serviceRepository.getAllServices(new FirestoreCallback<RepairService>() {
                            @Override
                            public void onSuccess(List<RepairService> services) {
                                Map<String, String> serviceNames = new HashMap<>();
                                for (RepairService s : services) serviceNames.put(s.serviceId, s.name);

                                List<AppointmentDisplay> displayList = new ArrayList<>();
                                for (Appointment a : filtered) {
                                    displayList.add(new AppointmentDisplay(
                                            a.appointmentId,
                                            a.requestedDate,
                                            branchNames.getOrDefault(a.branchId, "Unknown Branch"),
                                            serviceNames.getOrDefault(a.serviceId, "Unknown Service"),
                                            a.status
                                    ));
                                }

                                recyclerView.setAdapter(new AppointmentAdapter(displayList, appointment -> {
                                    Intent intent = new Intent(AppointmentsActivity.this, AppointmentDetailActivity.class);
                                    intent.putExtra("APPOINTMENT_ID", appointment.appointmentId);
                                    startActivity(intent);
                                }));
                            }

                            @Override
                            public void onFailure(Exception e) {}
                        });
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