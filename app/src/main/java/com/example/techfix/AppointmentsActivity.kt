package com.example.techfix

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.data.AppDatabase
import com.example.techfix.ui.AppointmentAdapter
import com.example.techfix.ui.AppointmentDisplay
import kotlinx.coroutines.launch

class AppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val userId = intent.getIntExtra("USER_ID", -1)
        val filterCompleted = intent.getBooleanExtra("FILTER_COMPLETED", false)

        findViewById<TextView>(R.id.tvAppointmentsTitle).text =
            if (filterCompleted) "Repair History" else "My Appointments"

        val recyclerView = findViewById<RecyclerView>(R.id.rvAppointments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val emptyState = findViewById<TextView>(R.id.tvEmptyState)

        val db = AppDatabase.getDatabase(applicationContext)

        lifecycleScope.launch {
            val allAppointments = db.appointmentDao().getAppointmentsByCustomer(userId)
            val filtered = if (filterCompleted) {
                allAppointments.filter { it.status == "Completed" }
            } else {
                allAppointments
            }

            if (filtered.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                emptyState.text = if (filterCompleted) "No completed repairs yet." else "No appointments yet."
                return@launch
            }

            val displayList = filtered.map { appointment ->
                val branch = db.branchDao().getBranchById(appointment.branchId)
                val service = db.repairServiceDao().getServiceById(appointment.serviceId)
                AppointmentDisplay(
                    appointmentId = appointment.appointmentId,
                    date = appointment.requestedDate,
                    branchName = branch?.name ?: "Unknown Branch",
                    serviceName = service?.name ?: "Unknown Service",
                    status = appointment.status
                )
            }

            recyclerView.adapter = AppointmentAdapter(displayList)
        }
    }
}