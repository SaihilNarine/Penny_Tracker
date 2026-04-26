package com.example.pennytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_goals")
data class MonthlyGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val minGoal: Double,
    val maxGoal: Double
)
