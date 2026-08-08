package com.example.techfix

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

        findViewById<TextView>(R.id.tvCategoryTitle).text = categoryName

        val recyclerView = findViewById<RecyclerView>(R.id.rvServices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            val services = db.repairServiceDao().getServicesByCategory(categoryId)
            recyclerView.adapter = ServiceAdapter(services) { service ->
                // TODO: navigate to booking screen once it's built
            }
        }
    }
}