package com.example.techfix

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.techfix.data.AppDatabase
import com.example.techfix.data.entity.Payment
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var appointmentId: Int = -1
    private var servicePrice: Double = 0.0
    private var currentPhotoUri: Uri? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchCamera() else Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoUri != null) {
            findViewById<ImageView>(R.id.ivRepairPhoto).setImageURI(currentPhotoUri)
            lifecycleScope.launch {
                db.appointmentDao().updateImage(appointmentId, currentPhotoUri.toString())
            }
            Toast.makeText(this, "Photo attached", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)

        db = AppDatabase.getDatabase(applicationContext)
        appointmentId = intent.getIntExtra("APPOINTMENT_ID", -1)

        loadAppointmentDetails()

        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        findViewById<Button>(R.id.btnPayNow).setOnClickListener {
            processPayment()
        }
    }

    private fun loadAppointmentDetails() {
        lifecycleScope.launch {
            val appointment = db.appointmentDao().getAppointmentById(appointmentId) ?: return@launch
            val branch = db.branchDao().getBranchById(appointment.branchId)
            val service = db.repairServiceDao().getServiceById(appointment.serviceId)
            servicePrice = service?.price ?: 0.0

            findViewById<TextView>(R.id.tvDetailService).text = service?.name ?: "Service"
            findViewById<TextView>(R.id.tvDetailBranch).text = branch?.name ?: "Branch"
            findViewById<TextView>(R.id.tvDetailDate).text = appointment.requestedDate
            findViewById<TextView>(R.id.tvDetailStatus).text = appointment.status

            appointment.imageUri?.let { uriString ->
                findViewById<ImageView>(R.id.ivRepairPhoto).setImageURI(Uri.parse(uriString))
            }

            val payment = db.paymentDao().getPaymentByAppointment(appointmentId)
            val paymentStatusView = findViewById<TextView>(R.id.tvPaymentStatus)
            val payButton = findViewById<Button>(R.id.btnPayNow)
            if (payment != null && payment.status == "Paid") {
                paymentStatusView.text = "Paid on ${payment.paymentDate} via ${payment.method}"
                payButton.isEnabled = false
                payButton.text = "Already Paid"
            } else {
                paymentStatusView.text = "Rs. %.2f — Not Paid".format(servicePrice)
            }
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        val photoFile = File.createTempFile(
            "repair_${appointmentId}_",
            ".jpg",
            getExternalFilesDir("Pictures")
        )
        currentPhotoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        takePictureLauncher.launch(currentPhotoUri)
    }

    private fun processPayment() {
        lifecycleScope.launch {
            val existing = db.paymentDao().getPaymentByAppointment(appointmentId)
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (existing == null) {
                db.paymentDao().insertPayment(
                    Payment(
                        appointmentId = appointmentId,
                        amount = servicePrice,
                        status = "Paid",
                        paymentDate = today,
                        method = "Cash"
                    )
                )
            } else {
                db.paymentDao().markAsPaid(existing.paymentId, "Paid", today)
            }

            Toast.makeText(this@AppointmentDetailActivity, "Payment successful!", Toast.LENGTH_SHORT).show()
            loadAppointmentDetails()
        }
    }
}