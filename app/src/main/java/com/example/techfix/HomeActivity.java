package com.example.techfix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.AppDatabase;
import com.example.techfix.data.DataSeeder;
import com.example.techfix.data.entity.DeviceCategory;
import com.example.techfix.ui.CategoryAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvLocation;

    private final ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fine = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean coarse = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                if (Boolean.TRUE.equals(fine) || Boolean.TRUE.equals(coarse)) {
                    getCurrentLocation();
                } else {
                    tvLocation.setText("Location permission denied");
                    Toast.makeText(this, "Location permission is required to get your GPS location", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tvLocation = findViewById(R.id.tvLocation);
        findViewById(R.id.btnGetLocation).setOnClickListener(v -> requestLocation());

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null) userName = "User";
        int userId = getIntent().getIntExtra("USER_ID", -1);

        ((TextView) findViewById(R.id.tvWelcome)).setText("Welcome, " + userName + "!");

        RecyclerView rv = findViewById(R.id.rvCategories);
        rv.setLayoutManager(new LinearLayoutManager(this));
        executor.execute(() -> {
            DataSeeder.seedIfEmpty(db);
            List<DeviceCategory> cats = db.deviceCategoryDao().getAllCategories();
            main.post(() -> rv.setAdapter(new CategoryAdapter(cats, c -> {
                Intent i = new Intent(this, ServicesActivity.class);
                i.putExtra("CATEGORY_ID", c.getCategoryId());
                i.putExtra("CATEGORY_NAME", c.getName());
                i.putExtra("USER_ID", userId);
                startActivity(i);
            })));
        });

        findViewById(R.id.btnMyAppointments).setOnClickListener(v -> openAppointments(userId, false));
        findViewById(R.id.btnRepairHistory).setOnClickListener(v -> openAppointments(userId, true));
        findViewById(R.id.btnAdminPanel).setOnClickListener(v -> startActivity(new Intent(this, AdminActivity.class)));
    }

    private void requestLocation() {
        boolean fineGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (fineGranted || coarseGranted) {
            getCurrentLocation();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void getCurrentLocation() {
        boolean fineGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (!fineGranted && !coarseGranted) return;

        tvLocation.setText("Getting your GPS location...");
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        fusedLocationClient.getCurrentLocation(
                fineGranted ? Priority.PRIORITY_HIGH_ACCURACY : Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.getToken()
        ).addOnSuccessListener(location -> {
            if (location != null) {
                displayLocation(location);
            } else {
                tvLocation.setText("Unable to get GPS location. Please try again.");
            }
        }).addOnFailureListener(e -> tvLocation.setText("Unable to get GPS location: " + e.getMessage()));
    }

    private void displayLocation(@NonNull Location location) {
        tvLocation.setText(String.format(
                java.util.Locale.US,
                "GPS Location:\nLatitude: %.6f\nLongitude: %.6f",
                location.getLatitude(), location.getLongitude()
        ));
    }

    private void openAppointments(int id, boolean completed) {
        Intent i = new Intent(this, AppointmentsActivity.class);
        i.putExtra("USER_ID", id);
        i.putExtra("FILTER_COMPLETED", completed);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }
}
