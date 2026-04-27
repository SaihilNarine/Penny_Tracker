package com.example.pennytracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    //global declarations
    private lateinit var btnLogout: Button
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var tvTotalSpent: TextView
    private lateinit var tvBudget: TextView
    private lateinit var progressBudget: ProgressBar
    private lateinit var categoryContainer: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        //typecasting
        btnLogout = findViewById(R.id.btnLogout)
        bottomNav = findViewById(R.id.bottomNav)
        tvTotalSpent = findViewById(R.id.tvTotalSpent)
        tvBudget = findViewById(R.id.tvBudget)
        progressBudget = findViewById(R.id.progressBudget)
        categoryContainer = findViewById(R.id.categoryContainer)


        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging you out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    // Home
                    true
                }
                R.id.nav_expenses -> {
                    val intent = Intent(this, Expenses::class.java)
                    startActivity(intent)
                    //Expense
                    true
                }
                R.id.nav_graphs -> {
                    val intent = Intent(this, Graphs::class.java)
                    startActivity(intent)
                    //Graphs
                    true
                }
                R.id.nav_budget -> {
                    val intent = Intent(this, Budget::class.java)
                    startActivity(intent)
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

        val totalSpent = 500f
        val budget = 2000f

        tvTotalSpent.text = "Total Spent: R$totalSpent"
        tvBudget.text = "Budget: R$budget"

        val percent = (totalSpent / budget * 100).toInt()
        progressBudget.progress = percent

        // Example calls to addCategory
        addCategory("Food", 150f, 500f)
        addCategory("Transport", 100f, 300f)
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    fun addCategory(name: String, spent: Float, limit: Float) {
        val view = layoutInflater.inflate(R.layout.item_category, null)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvAmount = view.findViewById<TextView>(R.id.tvAmount)
        val progress = view.findViewById<ProgressBar>(R.id.progressCategory)

        tvName.text = name
        tvAmount.text = "R$spent / R$limit"
        progress.progress = ((spent / limit) * 100).toInt()

        categoryContainer.addView(view)
    }
}