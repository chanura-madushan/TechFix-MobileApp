package com.example.techfix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.R

class AppointmentAdapter(
    private val appointments: List<AppointmentDisplay>,
    private val onAppointmentClick: (AppointmentDisplay) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvAppointmentDate)
        val tvBranch: TextView = view.findViewById(R.id.tvAppointmentBranch)
        val tvStatus: TextView = view.findViewById(R.id.tvAppointmentStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.tvDate.text = "${appointment.serviceName} — ${appointment.date}"
        holder.tvBranch.text = appointment.branchName
        holder.tvStatus.text = appointment.status
        holder.itemView.setOnClickListener { onAppointmentClick(appointment) }
    }

    override fun getItemCount(): Int = appointments.size
}