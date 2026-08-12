package com.example.techfix

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.data.AppDatabase
import com.example.techfix.ui.AdminAppointmentAdapter
import com.example.techfix.ui.AdminAppointmentDisplay
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = AppDatabase.getDatabase(applicationContext)
        recyclerView = findViewById(R.id.rvAdminAppointments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadAppointments()
    }

    private fun loadAppointments() {
        val emptyState = findViewById<TextView>(R.id.tvAdminEmptyState)

        lifecycleScope.launch {
            val allAppointments = db.appointmentDao().getActiveAppointments()

            if (allAppointments.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                return@launch
            } else {
                emptyState.visibility = View.GONE
            }

            val displayList = allAppointments.map { appointment ->
                val customer = db.userDao().getUserById(appointment.customerId)
                val branch = db.branchDao().getBranchById(appointment.branchId)
                val service = db.repairServiceDao().getServiceById(appointment.serviceId)
                AdminAppointmentDisplay(
                    appointmentId = appointment.appointmentId,
                    customerName = customer?.name ?: "Unknown Customer",
                    serviceName = service?.name ?: "Unknown Service",
                    branchName = branch?.name ?: "Unknown Branch",
                    date = appointment.requestedDate,
                    status = appointment.status
                )
            }

            recyclerView.adapter = AdminAppointmentAdapter(displayList) { appointment, newStatus ->
                lifecycleScope.launch {
                    db.appointmentDao().updateStatus(appointment.appointmentId, newStatus)
                    Toast.makeText(
                        this@AdminActivity,
                        "${appointment.customerName}'s appointment updated to $newStatus",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadAppointments() // refresh list
                }
            }
        }
    }
}