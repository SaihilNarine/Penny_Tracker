package com.example.pennytracker

import data.database.AppDatabase
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Achievements : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var db: AppDatabase

    data class Achievement(
        val emoji: String,
        val title: String,
        val description: String,
        var unlocked: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_achievements)

        bottomNav = findViewById(R.id.bottomNav)
        db = AppDatabase.getDatabase(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val expenses    = db.expenseDao().getAllExpenses()
            val goal        = db.monthlyGoalDao().getGoal()
            val totalSpent  = db.expenseDao().getTotalSpent() ?: 0f
            val categories  = db.expenseDao().getCategoryTotals()

            val achievements = listOf(
                Achievement("🎉", "First Step",       "Add your first expense",
                    unlocked = expenses.isNotEmpty()),
                Achievement("🎯", "Goal Getter",      "Set your first monthly goal",
                    unlocked = goal != null),
                Achievement("📂", "Category King",    "Use 3 or more expense categories",
                    unlocked = categories.size >= 3),
                Achievement("💸", "Big Spender",      "Spend over R1,000 in total",
                    unlocked = totalSpent >= 1000f),
                Achievement("💰", "Budget Boss",      "Stay under your max goal",
                    unlocked = goal != null && totalSpent <= (goal.maxGoal.toFloat())),
                Achievement("🪙", "Penny Pilot",      "Stay under your minimum goal",
                    unlocked = goal != null && totalSpent <= (goal.minGoal.toFloat()))
            )

            runOnUiThread {
                val container = findViewById<LinearLayout>(R.id.achievementsContainer)
                achievements.forEach { a -> container.addView(buildCard(a)) }
            }
        }

        setupBottomNav()
    }

    private fun buildCard(a: Achievement): LinearLayout {
        val dp = resources.displayMetrics.density

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding((16 * dp).toInt(), (12 * dp).toInt(), (16 * dp).toInt(), (12 * dp).toInt())
            setBackgroundColor(if (a.unlocked) Color.parseColor("#FFF8E7") else Color.parseColor("#F5F5F5"))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = (12 * dp).toInt()
            layoutParams = params
            // Rounded card look via elevation
            elevation = 2 * dp
        }

        val tvEmoji = TextView(this).apply {
            text = a.emoji
            textSize = 28f
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.rightMargin = (12 * dp).toInt()
            layoutParams = params
            alpha = if (a.unlocked) 1f else 0.35f
        }

        val textBlock = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val tvTitle = TextView(this).apply {
            text = a.title
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(if (a.unlocked) Color.parseColor("#E7A834") else Color.parseColor("#9E9E9E"))
        }

        val tvDesc = TextView(this).apply {
            text = a.description
            textSize = 13f
            setTextColor(Color.parseColor("#546E7A"))
        }

        val tvStatus = TextView(this).apply {
            text = if (a.unlocked) "✓ Unlocked" else "Locked"
            textSize = 12f
            setTextColor(if (a.unlocked) Color.parseColor("#1D9E75") else Color.parseColor("#BDBDBD"))
            gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        textBlock.addView(tvTitle)
        textBlock.addView(tvDesc)
        card.addView(tvEmoji)
        card.addView(textBlock)
        card.addView(tvStatus)

        return card
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_achievements
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
                R.id.nav_achievements -> true
                R.id.nav_settings -> {
                    startActivity(Intent(this, Settings::class.java)); finish(); true
                }
                else -> false
            }
        }
    }
}