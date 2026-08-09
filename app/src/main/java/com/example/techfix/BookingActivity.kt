package com.example.techfix

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.techfix.data.AppDatabase
import com.example.techfix.data.entity.Appointment
import com.example.techfix.data.entity.Branch
import com.example.techfix.util.LocationUtils
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var serviceId: Int = -1
    private var customerId: Int = -1
    private var nearestBranch: Branch? = null

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fetchLocationAndFindBranch()
        } else {
            Toast.makeText(this, "Location permission is required to find the nearest branch", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        db = AppDatabase.getDatabase(applicationContext)
        serviceId = intent.getIntExtra("SERVICE_ID", -1)
        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        val serviceName = intent.getStringExtra("SERVICE_NAME") ?: ""
        val servicePrice = intent.getDoubleExtra("SERVICE_PRICE", 0.0)

        findViewById<TextView>(R.id.tvServiceName).text = serviceName
        findViewById<TextView>(R.id.tvServicePrice).text = "Rs. %.2f".format(servicePrice)

        findViewById<Button>(R.id.btnFindBranch).setOnClickListener {
            checkPermissionAndFetchLocation()
        }

        findViewById<Button>(R.id.btnConfirmBooking).setOnClickListener {
            confirmBooking()
        }
    }

    private fun checkPermissionAndFetchLocation() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            fetchLocationAndFindBranch()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchLocationAndFindBranch() {
        val statusView = findViewById<TextView>(R.id.tvBranchStatus)
        statusView.text = "Getting your location..."

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    statusView.text = "Couldn't get location. Make sure GPS is enabled and try again."
                    return@addOnSuccessListener
                }

                lifecycleScope.launch {
                    val branches = db.branchDao().getAllBranches()
                    if (branches.isEmpty()) {
                        statusView.text = "No branches available."
                        return@launch
                    }

                    val closest = branches.minByOrNull {
                        LocationUtils.distanceKm(location.latitude, location.longitude, it.latitude, it.longitude)
                    }

                    nearestBranch = closest
                    if (closest != null) {
                        val distance = LocationUtils.distanceKm(
                            location.latitude, location.longitude, closest.latitude, closest.longitude
                        )
                        statusView.text = "Nearest branch: ${closest.name} (%.1f km away)".format(distance)
                        findViewById<Button>(R.id.btnConfirmBooking).isEnabled = true
                    }
                }
            }
        } catch (e: SecurityException) {
            statusView.text = "Location permission error. Please try again."
        }
    }

    private fun confirmBooking() {
        val date = findViewById<EditText>(R.id.etDate).text.toString().trim()
        val branch = nearestBranch

        if (date.isEmpty()) {
            Toast.makeText(this, "Please enter a preferred date", Toast.LENGTH_SHORT).show()
            return
        }
        if (branch == null) {
            Toast.makeText(this, "Please find your nearest branch first", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            db.appointmentDao().insertAppointment(
                Appointment(
                    customerId = customerId,
                    branchId = branch.branchId,
                    serviceId = serviceId,
                    requestedDate = date,
                    status = "Pending"
                )
            )
            Toast.makeText(this@BookingActivity, "Appointment booked at ${branch.name}!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}