package com.example.techfix

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        findViewById<TextView>(R.id.tvWelcome).text = "Welcome, $userName!"
    }
}