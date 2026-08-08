package com.example.techfix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.R
import com.example.techfix.data.entity.RepairService

class ServiceAdapter(
    private val services: List<RepairService>,
    private val onServiceClick: (RepairService) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvServiceName)
        val tvDescription: TextView = view.findViewById(R.id.tvServiceDescription)
        val tvPrice: TextView = view.findViewById(R.id.tvServicePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.tvName.text = service.name
        holder.tvDescription.text = service.description
        holder.tvPrice.text = "Rs. %.2f".format(service.price)
        holder.itemView.setOnClickListener { onServiceClick(service) }
    }

    override fun getItemCount(): Int = services.size
}