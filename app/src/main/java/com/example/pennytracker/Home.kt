package com.example.pennytracker

import data.database.AppDatabase
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {

    //global declarations
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var tvTotalSpent: TextView
    private lateinit var tvBudget: TextView
    private lateinit var progressBudget: ProgressBar
    private lateinit var categoryContainer: LinearLayout

    private lateinit var db: AppDatabase

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        //typecasting
        bottomNav = findViewById(R.id.bottomNav)
        tvTotalSpent = findViewById(R.id.tvTotalSpent)
        tvBudget = findViewById(R.id.tvBudget)
        progressBudget = findViewById(R.id.progressBudget)
        categoryContainer = findViewById(R.id.categoryContainer)

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

        db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {

            // 1. Get total spent
            val totalSpent = db.expenseDao().getTotalSpent() ?: 0f

            // 2. Get budget
            val goal = db.monthlyGoalDao().getGoal()
            val budget = goal?.maxGoal?.toFloat() ?: 0f

            tvTotalSpent.text = "Total Spent: R$totalSpent"
            tvBudget.text = "Budget: R$budget"

            // 3. Progress bar
            if (budget > 0) {
                val percent = ((totalSpent / budget) * 100).toInt()
                progressBudget.progress = percent
            }

            // 4. Category totals
            val categories = db.expenseDao().getCategoryTotals()

            // Clear old views first
            categoryContainer.removeAllViews()

            categories.forEach {
                addCategory(it.category, it.total, 500f) // temporary limit
            }
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.logout -> {
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}