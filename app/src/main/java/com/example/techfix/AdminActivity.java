package com.example.techfix;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.AppointmentRepository;
import com.example.techfix.data.BranchRepository;
import com.example.techfix.data.FirestoreCallback;
import com.example.techfix.data.FirestoreSingleCallback;
import com.example.techfix.data.RepairServiceRepository;
import com.example.techfix.data.UserRepository;
import com.example.techfix.model.Appointment;
import com.example.techfix.model.Branch;
import com.example.techfix.model.RepairService;
import com.example.techfix.model.User;
import com.example.techfix.ui.AdminAppointmentAdapter;
import com.example.techfix.ui.AdminAppointmentDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;
    private BranchRepository branchRepository;
    private RepairServiceRepository serviceRepository;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRepository = new UserRepository();
        String userId = getIntent().getStringExtra("USER_ID");

        if (userId == null) {
            Toast.makeText(this, "Access denied.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository.getUserById(userId, new FirestoreSingleCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (!"admin".equals(user.role)) {
                    Toast.makeText(AdminActivity.this, "Access denied. Admin accounts only.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                initAdminScreen();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminActivity.this, "Access denied.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initAdminScreen() {
        setContentView(R.layout.activity_admin);

        appointmentRepository = new AppointmentRepository();
        branchRepository = new BranchRepository();
        serviceRepository = new RepairServiceRepository();

        recyclerView = findViewById(R.id.rvAdminAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAppointments();
    }

    private void loadAppointments() {
        TextView emptyState = findViewById(R.id.tvAdminEmptyState);

        appointmentRepository.getActiveAppointments(new FirestoreCallback<Appointment>() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                if (appointments.isEmpty()) {
                    emptyState.setVisibility(View.VISIBLE);
                    return;
                }
                emptyState.setVisibility(View.GONE);

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

                                Map<String, String> customerNames = new HashMap<>();
                                AtomicInteger remaining = new AtomicInteger(appointments.size());

                                for (Appointment a : appointments) {
                                    userRepository.getUserById(a.customerId, new FirestoreSingleCallback<User>() {
                                        @Override
                                        public void onSuccess(User user) {
                                            customerNames.put(a.customerId, user.name);
                                            checkDone();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            customerNames.put(a.customerId, "Unknown Customer");
                                            checkDone();
                                        }

                                        private void checkDone() {
                                            if (remaining.decrementAndGet() == 0) {
                                                buildDisplayList(appointments, branchNames, serviceNames, customerNames);
                                            }
                                        }
                                    });
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

            @Override
            public void onFailure(Exception e) {}
        });
    }

    private void buildDisplayList(List<Appointment> appointments, Map<String, String> branchNames,
                                  Map<String, String> serviceNames, Map<String, String> customerNames) {
        List<AdminAppointmentDisplay> displayList = new ArrayList<>();
        for (Appointment a : appointments) {
            displayList.add(new AdminAppointmentDisplay(
                    a.appointmentId,
                    customerNames.getOrDefault(a.customerId, "Unknown Customer"),
                    serviceNames.getOrDefault(a.serviceId, "Unknown Service"),
                    branchNames.getOrDefault(a.branchId, "Unknown Branch"),
                    a.requestedDate,
                    a.status
            ));
        }

        recyclerView.setAdapter(new AdminAppointmentAdapter(displayList, (appointment, newStatus) -> {
            appointmentRepository.updateStatus(appointment.appointmentId, newStatus);
            Toast.makeText(AdminActivity.this,
                    appointment.customerName + "'s appointment updated to " + newStatus, Toast.LENGTH_SHORT).show();
            loadAppointments();
        }));
    }
}