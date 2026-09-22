package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.DataSeeder;
import com.example.techfix.data.DeviceCategoryRepository;
import com.example.techfix.data.FirestoreCallback;
import com.example.techfix.data.FirestoreSingleCallback;
import com.example.techfix.data.UserRepository;
import com.example.techfix.model.DeviceCategory;
import com.example.techfix.model.User;
import com.example.techfix.ui.CategoryAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null) userName = "User";
        userId = getIntent().getStringExtra("USER_ID");

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + userName + "!");

        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DataSeeder.seedIfEmpty();

        DeviceCategoryRepository categoryRepository = new DeviceCategoryRepository();
        categoryRepository.getAllCategories(new FirestoreCallback<DeviceCategory>() {
            @Override
            public void onSuccess(List<DeviceCategory> categories) {
                recyclerView.setAdapter(new CategoryAdapter(categories, category -> {
                    Intent intent = new Intent(HomeActivity.this, ServicesActivity.class);
                    intent.putExtra("CATEGORY_ID", category.categoryId);
                    intent.putExtra("CATEGORY_NAME", category.name);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                }));
            }

            @Override
            public void onFailure(Exception e) {}
        });

        findViewById(R.id.btnMyAppointments).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AppointmentsActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("FILTER_COMPLETED", false);
            startActivity(intent);
        });

        findViewById(R.id.btnRepairHistory).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AppointmentsActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("FILTER_COMPLETED", true);
            startActivity(intent);
        });

        Button btnAdminPanel = findViewById(R.id.btnAdminPanel);
        btnAdminPanel.setVisibility(View.GONE); // hidden until role is confirmed

        btnAdminPanel.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        checkAdminRole(btnAdminPanel);
    }

    private void checkAdminRole(Button btnAdminPanel) {
        if (userId == null) return;

        UserRepository userRepository = new UserRepository();
        userRepository.getUserById(userId, new FirestoreSingleCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if ("admin".equals(user.role)) {
                    btnAdminPanel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // If the role check fails, the button stays hidden — safe default.
            }
        });
    }
}