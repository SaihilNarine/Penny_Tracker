package com.example.pennytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.FirebaseDatabase
import data.ChartData
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val category: String,
    val amount: Double,
    val date: String,
    val description: String,
    val photoUri: String? =null



)
