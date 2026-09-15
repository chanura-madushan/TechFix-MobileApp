package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.FirestoreCallback;
import com.example.techfix.data.RepairServiceRepository;
import com.example.techfix.model.RepairService;
import com.example.techfix.ui.ServiceAdapter;

import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        String userId = getIntent().getStringExtra("USER_ID");
        if (categoryName == null) categoryName = "Services";

        TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        tvTitle.setText(categoryName);

        RecyclerView recyclerView = findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String finalUserId = userId;
        RepairServiceRepository serviceRepository = new RepairServiceRepository();
        serviceRepository.getServicesByCategory(categoryId, new FirestoreCallback<RepairService>() {
            @Override
            public void onSuccess(List<RepairService> services) {
                recyclerView.setAdapter(new ServiceAdapter(services, service -> {
                    Intent intent = new Intent(ServicesActivity.this, BookingActivity.class);
                    intent.putExtra("SERVICE_ID", service.serviceId);
                    intent.putExtra("SERVICE_NAME", service.name);
                    intent.putExtra("SERVICE_PRICE", service.price);
                    intent.putExtra("CUSTOMER_ID", finalUserId);
                    startActivity(intent);
                }));
            }

            @Override
            public void onFailure(Exception e) {}
        });
    }
}