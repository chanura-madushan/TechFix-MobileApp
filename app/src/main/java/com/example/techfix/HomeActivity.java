package com.example.techfix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.data.AppDatabase;
import com.example.techfix.data.DataSeeder;
import com.example.techfix.data.entity.DeviceCategory;
import com.example.techfix.ui.CategoryAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnGetLocation).setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class)));

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null) userName = "User";
        int userId = getIntent().getIntExtra("USER_ID", -1);

        ((TextView) findViewById(R.id.tvWelcome)).setText("Welcome, " + userName + "!");

        RecyclerView rv = findViewById(R.id.rvCategories);
        rv.setLayoutManager(new LinearLayoutManager(this));
        executor.execute(() -> {
            DataSeeder.seedIfEmpty(db);
            List<DeviceCategory> cats = db.deviceCategoryDao().getAllCategories();
            main.post(() -> rv.setAdapter(new CategoryAdapter(cats, c -> {
                Intent i = new Intent(this, ServicesActivity.class);
                i.putExtra("CATEGORY_ID", c.getCategoryId());
                i.putExtra("CATEGORY_NAME", c.getName());
                i.putExtra("USER_ID", userId);
                startActivity(i);
            })));
        });

        findViewById(R.id.btnMyAppointments).setOnClickListener(v -> openAppointments(userId, false));
        findViewById(R.id.btnRepairHistory).setOnClickListener(v -> openAppointments(userId, true));
        findViewById(R.id.btnAdminPanel).setOnClickListener(v -> startActivity(new Intent(this, AdminActivity.class)));
    }

    private void openAppointments(int id, boolean completed) {
        Intent i = new Intent(this, AppointmentsActivity.class);
        i.putExtra("USER_ID", id);
        i.putExtra("FILTER_COMPLETED", completed);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }
}
