package com.example.pennytracker

import data.database.AppDatabase
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Settings : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private lateinit var switchNotifications: Switch
    private lateinit var btnClearData: Button
    private lateinit var btnExportData: Button
    private lateinit var btnAbout: Button
    private lateinit var btnLogout: Button
    private lateinit var tvAppVersion: TextView
    private lateinit var tvUsername: TextView

    private lateinit var prefs: SharedPreferences
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        // Initialize
        prefs = getSharedPreferences("settings", MODE_PRIVATE)
        db = AppDatabase.getDatabase(this)

        // Views
        bottomNav = findViewById(R.id.bottomNav)

        switchNotifications = findViewById(R.id.switchNotifications)
        btnClearData = findViewById(R.id.btnClearData)
        btnExportData = findViewById(R.id.btnExportData)
        btnAbout = findViewById(R.id.btnAbout)
        btnLogout = findViewById(R.id.btnLogout)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        tvUsername = findViewById(R.id.tvUsername)

        // Load saved settings

        switchNotifications.isChecked = prefs.getBoolean("notifications", true)
        tvAppVersion.text = "Version 1.0.0"

        // Dark Mode Toggle



        // Notifications Toggle
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean("notifications", isChecked) }
            val msg = if (isChecked) "Notifications enabled" else "Notifications disabled"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Clear All Data
        btnClearData.setOnClickListener {
            showClearDataDialog()
        }

        // Export Data (placeholder)
        btnExportData.setOnClickListener {
            Toast.makeText(this, "Export feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // About Dialog
        btnAbout.setOnClickListener {
            showAboutDialog()
        }

        // Logout
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                    true
                }
                R.id.nav_expenses -> {
                    startActivity(Intent(this, Expenses::class.java))
                    finish()
                    true
                }
                R.id.nav_graphs -> {
                    startActivity(Intent(this, Graphs::class.java))
                    finish()
                    true
                }
                R.id.nav_budget -> {
                    startActivity(Intent(this, Budget::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun applyDarkMode(enabled: Boolean) {
        val mode = if (enabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun showClearDataDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear All Data")
            .setMessage("This will permanently delete all expenses, goals, and achievements. This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                lifecycleScope.launch {
                    db.clearAllTables()
                    prefs.edit { clear() }
                    runOnUiThread {
                        Toast.makeText(this@Settings, "All data cleared", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Settings, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About PennyTracker")
            .setMessage("PennyTracker v1.0.0\n\nA personal finance management app to help you track expenses, manage budgets, and achieve your savings goals.\n\nDeveloped with ❤️")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}