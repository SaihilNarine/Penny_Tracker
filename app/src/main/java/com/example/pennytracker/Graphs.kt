package com.example.pennytracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pennytracker.views.BarChartView
import com.example.pennytracker.views.LineChartView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Graphs : AppCompatActivity() {

    //Global VAR
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        val barChart = findViewById<BarChartView>(R.id.barChart)
        val lineChart = findViewById<LineChartView>(R.id.lineChart)

        barChart.data = listOf(120f, 200f, 150f, 300f, 250f)
        lineChart.data = listOf(50f, 100f, 80f, 200f, 150f)

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
    }
}