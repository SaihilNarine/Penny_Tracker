package com.example.pennytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Home : AppCompatActivity() {

    //global declarations
    private lateinit var btnExpenses: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        //typecasting
        btnExpenses = findViewById(R.id.btnExpenses)
        btnSettings = findViewById(R.id.btnSettings)
        btnLogout = findViewById(R.id.btnLogout)

        btnExpenses.setOnClickListener {
            Toast.makeText(this, "Open the expenses screen", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Expenses::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this, "Open the Settings screen", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging you out", Toast.LENGTH_SHORT).show()

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}