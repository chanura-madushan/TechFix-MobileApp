package com.example.techfix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.R

class AdminAppointmentAdapter(
    private val appointments: List<AdminAppointmentDisplay>,
    private val onUpdateStatus: (AdminAppointmentDisplay, String) -> Unit
) : RecyclerView.Adapter<AdminAppointmentAdapter.AdminViewHolder>() {

    private val statusOptions = listOf("Pending", "In Progress", "Completed")

    class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCustomer: TextView = view.findViewById(R.id.tvAdminCustomer)
        val tvService: TextView = view.findViewById(R.id.tvAdminService)
        val tvBranchDate: TextView = view.findViewById(R.id.tvAdminBranchDate)
        val spinnerStatus: Spinner = view.findViewById(R.id.spinnerStatus)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdateStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_appointment, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.tvCustomer.text = appointment.customerName
        holder.tvService.text = appointment.serviceName
        holder.tvBranchDate.text = "${appointment.branchName} — ${appointment.date}"

        val spinnerAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_dropdown_item,
            statusOptions
        )
        holder.spinnerStatus.adapter = spinnerAdapter
        val currentIndex = statusOptions.indexOf(appointment.status).coerceAtLeast(0)
        holder.spinnerStatus.setSelection(currentIndex)

        holder.btnUpdate.setOnClickListener {
            val selectedStatus = holder.spinnerStatus.selectedItem as String
            onUpdateStatus(appointment, selectedStatus)
        }
    }

    override fun getItemCount(): Int = appointments.size
}