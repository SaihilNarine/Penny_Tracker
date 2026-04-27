package com.example.pennytracker

import Data.database.AppDatabase
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pennytracker.views.BarChartView
import com.example.pennytracker.views.LineChartView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Graphs : AppCompatActivity() {

    //Global VAR
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        val barChart = findViewById<BarChartView>(R.id.barChart)
        val lineChart = findViewById<LineChartView>(R.id.lineChart)

        bottomNav = findViewById(R.id.bottomNav)

        db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {

            val categories = db.expenseDao().getCategoryTotals()
            val barData = categories.map { it.total }

            barChart.data = barData
            barChart.invalidate()

            val expenses = db.expenseDao().getAllExpenses()
            val lineData = expenses.map { it.amount.toFloat() }
            lineChart.data = lineData
            lineChart.invalidate()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
                R.id.nav_expenses -> {
                    startActivity(Intent(this, Expenses::class.java))
                    true
                }
                R.id.nav_graphs -> true
                R.id.nav_budget -> {
                    startActivity(Intent(this, Budget::class.java))
                    true
                }
                else -> false
            }
        }
    }
}