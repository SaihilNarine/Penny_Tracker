package com.example.pennytracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class Budget : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget)

        val prefs = getSharedPreferences("finance", 0)

        val etBudget = findViewById<EditText>(R.id.etBudget)
        val btnSaveBudget = findViewById<Button>(R.id.btnSaveBudget)
        val etCategoryName = findViewById<EditText>(R.id.etCategoryName)
        val etLimit = findViewById<EditText>(R.id.etLimit)
        val btnAddCategory = findViewById<Button>(R.id.btnAddCategory)
        val categoryList = findViewById<LinearLayout>(R.id.categoryList)

        btnSaveBudget.setOnClickListener {
            val amount = etBudget.text.toString().toFloatOrNull() ?: 0f
            prefs.edit { putFloat("budget", amount) }
        }

        btnAddCategory.setOnClickListener {
            val name = etCategoryName.text.toString()
            val limit = etLimit.text.toString().toFloatOrNull() ?: 0f

            val tv = TextView(this)
            tv.text = "$name: R$$limit"

            categoryList.addView(tv)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
