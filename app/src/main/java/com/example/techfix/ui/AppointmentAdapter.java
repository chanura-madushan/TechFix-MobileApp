package com.example.techfix.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    public interface OnAppointmentClickListener {
        void onAppointmentClick(AppointmentDisplay appointment);
    }

    private final List<AppointmentDisplay> appointments;
    private final OnAppointmentClickListener listener;

    public AppointmentAdapter(List<AppointmentDisplay> appointments, OnAppointmentClickListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvBranch, tvStatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvBranch = itemView.findViewById(R.id.tvAppointmentBranch);
            tvStatus = itemView.findViewById(R.id.tvAppointmentStatus);
        }
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentDisplay appointment = appointments.get(position);
        holder.tvDate.setText(appointment.serviceName + " — " + appointment.date);
        holder.tvBranch.setText(appointment.branchName);
        holder.tvStatus.setText(appointment.status);
        holder.itemView.setOnClickListener(v -> listener.onAppointmentClick(appointment));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}