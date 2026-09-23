package com.example.techfix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techfix.data.AppointmentRepository;
import com.example.techfix.data.BranchRepository;
import com.example.techfix.data.FirestoreCallback;
import com.example.techfix.data.FirestoreSingleCallback;
import com.example.techfix.model.Appointment;
import com.example.techfix.model.Branch;
import com.example.techfix.util.LocationUtils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.List;
import java.util.Locale;

public class BookingActivity extends BaseActivity {

    private String serviceId, customerId;
    private double servicePrice;
    private Branch nearestBranch;

    private final androidx.activity.result.ActivityResultLauncher<String> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    fetchLocationAndFindBranch();
                } else {
                    Toast.makeText(this, "Location permission is required to find the nearest branch", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        serviceId = getIntent().getStringExtra("SERVICE_ID");
        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        String serviceName = getIntent().getStringExtra("SERVICE_NAME");
        servicePrice = getIntent().getDoubleExtra("SERVICE_PRICE", 0.0);

        ((TextView) findViewById(R.id.tvServiceName)).setText(serviceName);
        ((TextView) findViewById(R.id.tvServicePrice)).setText(String.format(Locale.getDefault(), "Rs. %.2f", servicePrice));

        findViewById(R.id.btnFindBranch).setOnClickListener(v -> checkPermissionAndFetchLocation());
        findViewById(R.id.btnConfirmBooking).setOnClickListener(v -> confirmBooking());
    }

    private void checkPermissionAndFetchLocation() {
        boolean hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (hasPermission) {
            fetchLocationAndFindBranch();
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void fetchLocationAndFindBranch() {
        TextView statusView = findViewById(R.id.tvBranchStatus);
        statusView.setText("Getting your location...");

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        LocationServices.getFusedLocationProviderClient(this)
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        statusView.setText("Couldn't get location. Make sure GPS is enabled and try again.");
                        return;
                    }

                    BranchRepository branchRepository = new BranchRepository();
                    branchRepository.getAllBranches(new FirestoreCallback<Branch>() {
                        @Override
                        public void onSuccess(List<Branch> branches) {
                            if (branches.isEmpty()) {
                                statusView.setText("No branches available.");
                                return;
                            }

                            Branch closest = null;
                            double closestDistance = Double.MAX_VALUE;
                            for (Branch b : branches) {
                                double distance = LocationUtils.distanceKm(
                                        location.getLatitude(), location.getLongitude(), b.latitude, b.longitude);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    closest = b;
                                }
                            }

                            nearestBranch = closest;
                            statusView.setText(String.format(Locale.getDefault(),
                                    "Nearest branch: %s (%.1f km away)", closest.name, closestDistance));
                            findViewById(R.id.btnConfirmBooking).setEnabled(true);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            statusView.setText("Error loading branches: " + e.getMessage());
                        }
                    });
                })
                .addOnFailureListener(e -> statusView.setText("Failed to get location: " + e.getMessage()));
    }

    private void confirmBooking() {
        String date = ((EditText) findViewById(R.id.etDate)).getText().toString().trim();

        if (date.isEmpty()) {
            Toast.makeText(this, "Please enter a preferred date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nearestBranch == null) {
            Toast.makeText(this, "Please find your nearest branch first", Toast.LENGTH_SHORT).show();
            return;
        }

        Appointment appointment = new Appointment(customerId, nearestBranch.branchId, serviceId, date);

        AppointmentRepository appointmentRepository = new AppointmentRepository();
        appointmentRepository.insertAppointment(appointment, new FirestoreSingleCallback<Appointment>() {
            @Override
            public void onSuccess(Appointment result) {
                Toast.makeText(BookingActivity.this, "Appointment booked at " + nearestBranch.name + "!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BookingActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}