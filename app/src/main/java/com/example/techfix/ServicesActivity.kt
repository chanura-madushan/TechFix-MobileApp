package com.example.techfix

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.data.AppDatabase
import com.example.techfix.ui.ServiceAdapter
import kotlinx.coroutines.launch

class ServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Services"
        val userId = intent.getIntExtra("USER_ID", -1)

        findViewById<TextView>(R.id.tvCategoryTitle).text = categoryName

        val recyclerView = findViewById<RecyclerView>(R.id.rvServices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            val services = db.repairServiceDao().getServicesByCategory(categoryId)
            recyclerView.adapter = ServiceAdapter(services) { service ->
                val intent = Intent(this@ServicesActivity, BookingActivity::class.java)
                intent.putExtra("SERVICE_ID", service.serviceId)
                intent.putExtra("SERVICE_NAME", service.name)
                intent.putExtra("SERVICE_PRICE", service.price)
                intent.putExtra("CUSTOMER_ID", userId)
                startActivity(intent)
            }
        }
    }
}