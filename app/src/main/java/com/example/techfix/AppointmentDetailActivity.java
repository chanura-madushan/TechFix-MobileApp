package com.example.techfix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.techfix.data.AppointmentRepository;
import com.example.techfix.data.BranchRepository;
import com.example.techfix.data.FirestoreSingleCallback;
import com.example.techfix.data.PaymentRepository;
import com.example.techfix.data.RepairServiceRepository;
import com.example.techfix.model.Appointment;
import com.example.techfix.model.Branch;
import com.example.techfix.model.Payment;
import com.example.techfix.model.RepairService;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {

    private AppointmentRepository appointmentRepository;
    private PaymentRepository paymentRepository;
    private String appointmentId;
    private double servicePrice;
    private Uri currentPhotoUri;

    private final androidx.activity.result.ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) launchCamera();
                else Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            });

    private final androidx.activity.result.ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && currentPhotoUri != null) {
                    ((ImageView) findViewById(R.id.ivRepairPhoto)).setImageURI(currentPhotoUri);
                    appointmentRepository.updateImage(appointmentId, currentPhotoUri.toString());
                    Toast.makeText(this, "Photo attached", Toast.LENGTH_SHORT).show();
                }
            });

    private final androidx.activity.result.ActivityResultLauncher<Intent> paymentGatewayLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    updatePaymentSection();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        appointmentRepository = new AppointmentRepository();
        paymentRepository = new PaymentRepository();
        appointmentId = getIntent().getStringExtra("APPOINTMENT_ID");

        loadAppointmentDetails();

        findViewById(R.id.btnTakePhoto).setOnClickListener(v -> checkCameraPermissionAndLaunch());
        findViewById(R.id.btnPayNow).setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentDetailActivity.this, PaymentGatewayActivity.class);
            intent.putExtra("APPOINTMENT_ID", appointmentId);
            intent.putExtra("AMOUNT", servicePrice);
            paymentGatewayLauncher.launch(intent);
        });
    }

    private void loadAppointmentDetails() {
        appointmentRepository.getAppointmentById(appointmentId, new FirestoreSingleCallback<Appointment>() {
            @Override
            public void onSuccess(Appointment appointment) {
                BranchRepository branchRepository = new BranchRepository();
                RepairServiceRepository serviceRepository = new RepairServiceRepository();

                branchRepository.getBranchById(appointment.branchId, new FirestoreSingleCallback<Branch>() {
                    @Override
                    public void onSuccess(Branch branch) {
                        ((TextView) findViewById(R.id.tvDetailBranch)).setText(branch.name);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ((TextView) findViewById(R.id.tvDetailBranch)).setText("Branch");
                    }
                });

                serviceRepository.getServiceById(appointment.serviceId, new FirestoreSingleCallback<RepairService>() {
                    @Override
                    public void onSuccess(RepairService service) {
                        servicePrice = service.price;
                        ((TextView) findViewById(R.id.tvDetailService)).setText(service.name);
                        updatePaymentSection();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ((TextView) findViewById(R.id.tvDetailService)).setText("Service");
                    }
                });

                ((TextView) findViewById(R.id.tvDetailDate)).setText(appointment.requestedDate);
                ((TextView) findViewById(R.id.tvDetailStatus)).setText(appointment.status);

                if (appointment.imageUri != null && !appointment.imageUri.isEmpty()) {
                    ((ImageView) findViewById(R.id.ivRepairPhoto)).setImageURI(Uri.parse(appointment.imageUri));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AppointmentDetailActivity.this, "Failed to load appointment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePaymentSection() {
        TextView paymentStatusView = findViewById(R.id.tvPaymentStatus);
        Button payButton = findViewById(R.id.btnPayNow);

        paymentRepository.getPaymentByAppointment(appointmentId, new FirestoreSingleCallback<Payment>() {
            @Override
            public void onSuccess(Payment payment) {
                if ("Paid".equals(payment.status)) {
                    paymentStatusView.setText("Paid on " + payment.paymentDate + " via " + payment.method);
                    payButton.setEnabled(false);
                    payButton.setText("Already Paid");
                } else {
                    paymentStatusView.setText(String.format(Locale.getDefault(), "Rs. %.2f — Not Paid", servicePrice));
                }
            }

            @Override
            public void onFailure(Exception e) {
                // No payment record exists yet — normal for a fresh appointment.
                paymentStatusView.setText(String.format(Locale.getDefault(), "Rs. %.2f — Not Paid", servicePrice));
            }
        });
    }

    private void checkCameraPermissionAndLaunch() {
        boolean hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            launchCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        try {
            File photoFile = File.createTempFile(
                    "repair_" + appointmentId + "_",
                    ".jpg",
                    getExternalFilesDir("Pictures")
            );
            currentPhotoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );
            takePictureLauncher.launch(currentPhotoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Couldn't create photo file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}