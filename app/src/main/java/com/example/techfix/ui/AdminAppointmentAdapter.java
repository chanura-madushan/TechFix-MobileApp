package com.example.techfix.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;

import java.util.Arrays;
import java.util.List;

public class AdminAppointmentAdapter extends RecyclerView.Adapter<AdminAppointmentAdapter.AdminViewHolder> {

    public interface OnUpdateStatusListener {
        void onUpdateStatus(AdminAppointmentDisplay appointment, String newStatus);
    }

    private final List<AdminAppointmentDisplay> appointments;
    private final OnUpdateStatusListener listener;
    private final List<String> statusOptions = Arrays.asList("Pending", "In Progress", "Completed");

    public AdminAppointmentAdapter(List<AdminAppointmentDisplay> appointments, OnUpdateStatusListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvService, tvBranchDate;
        Spinner spinnerStatus;
        Button btnUpdate;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tvAdminCustomer);
            tvService = itemView.findViewById(R.id.tvAdminService);
            tvBranchDate = itemView.findViewById(R.id.tvAdminBranchDate);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_appointment, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        AdminAppointmentDisplay appointment = appointments.get(position);
        holder.tvCustomer.setText(appointment.customerName);
        holder.tvService.setText(appointment.serviceName);
        holder.tvBranchDate.setText(appointment.branchName + " — " + appointment.date);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statusOptions
        );
        holder.spinnerStatus.setAdapter(spinnerAdapter);
        int currentIndex = statusOptions.indexOf(appointment.status);
        holder.spinnerStatus.setSelection(Math.max(currentIndex, 0));

        holder.btnUpdate.setOnClickListener(v -> {
            String selectedStatus = (String) holder.spinnerStatus.getSelectedItem();
            listener.onUpdateStatus(appointment, selectedStatus);
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}