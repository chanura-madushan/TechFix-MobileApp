package com.example.techfix

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techfix.data.AppDatabase
import com.example.techfix.data.DataSeeder
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = AppDatabase.getDatabase(applicationContext)

        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        findViewById<TextView>(R.id.tvWelcome).text = "Welcome, $userName!"

        val recyclerView = findViewById<RecyclerView>(R.id.rvCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            DataSeeder.seedIfEmpty(db)
            val categories = db.deviceCategoryDao().getAllCategories()
            recyclerView.adapter = CategoryAdapter(categories) { category ->
                val intent = Intent(this@HomeActivity, ServicesActivity::class.java)
                intent.putExtra("CATEGORY_ID", category.categoryId)
                intent.putExtra("CATEGORY_NAME", category.name)
                startActivity(intent)
            }
        }
    }
}