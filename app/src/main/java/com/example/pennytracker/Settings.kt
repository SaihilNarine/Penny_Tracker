package com.example.pennytracker


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import data.database.AppDatabase
import kotlinx.coroutines.launch

class Settings : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        db = AppDatabase.getDatabase(this)
        bottomNav = findViewById(R.id.bottomNav)

        val etCurrency        = findViewById<EditText>(R.id.etCurrencySymbol)
        val btnSaveCurrency   = findViewById<Button>(R.id.btnSaveCurrency)
        val switchNotif       = findViewById<SwitchCompat>(R.id.switchNotifications)
        val btnClearExpenses  = findViewById<Button>(R.id.btnClearExpenses)
        val btnClearGoals     = findViewById<Button>(R.id.btnClearGoals)
        val btnLogout         = findViewById<Button>(R.id.btnLogout)

        // Load saved settings
        val prefs = getSharedPreferences("settings", 0)
        etCurrency.setText(prefs.getString("currency_symbol", "R"))
        switchNotif.isChecked = prefs.getBoolean("notifications_enabled", true)

        btnSaveCurrency.setOnClickListener {
            val symbol = etCurrency.text.toString().trim().ifEmpty { "R" }
            prefs.edit { putString("currency_symbol", symbol) }
            Toast.makeText(this, "Currency set to $symbol", Toast.LENGTH_SHORT).show()
        }

        switchNotif.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean("notifications_enabled", isChecked) }
        }

        btnClearExpenses.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear all expenses?")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Clear") { _, _ ->
                    lifecycleScope.launch {
                        db.expenseDao().deleteAllExpenses()
                        runOnUiThread {
                            Toast.makeText(this@Settings, "Expenses cleared", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        btnClearGoals.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear monthly goals?")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Clear") { _, _ ->
                    lifecycleScope.launch {
                        db.monthlyGoalDao().deleteAllGoals()
                        runOnUiThread {
                            Toast.makeText(this@Settings, "Goals cleared", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        btnLogout.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_settings
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, Home::class.java)); finish(); true
                }
                R.id.nav_expenses -> {
                    startActivity(Intent(this, Expenses::class.java)); finish(); true
                }
                R.id.nav_graphs -> {
                    startActivity(Intent(this, Graphs::class.java)); finish(); true
                }
                R.id.nav_budget -> {
                    startActivity(Intent(this, Budget::class.java)); finish(); true
                }
                R.id.nav_achievements -> {
                    startActivity(Intent(this, Achievements::class.java)); finish(); true
                }
                R.id.nav_settings -> true
                else -> false
            }
        }
    }
}