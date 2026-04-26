package com.example.pennytracker

import Data.database.AppDatabase
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pennytracker.data.Expense
import com.example.pennytracker.data.MonthlyGoal
import kotlinx.coroutines.launch

class Expenses : AppCompatActivity() {

    //global declarations
    private lateinit var edtAmount: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtCategory: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnPhoto: Button
    private lateinit var btnSaveExpenses: Button
    private lateinit var edtMinGoal: EditText
    private lateinit var edtMaxGoal: EditText
    private lateinit var btnSaveGoals: Button

    private lateinit var db: AppDatabase

    private var selectedPhotoUri: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses)

        //typecasting
        edtAmount = findViewById(R.id.edtAmount)
        edtDate = findViewById(R.id.edtDate)
        edtCategory = findViewById(R.id.edtCategory)
        edtDescription = findViewById(R.id.edtDescription)
        btnPhoto = findViewById(R.id.btnPhoto)
        btnSaveExpenses = findViewById(R.id.btnSaveExpenses)
        edtMinGoal = findViewById(R.id.edtMinGoal)
        edtMaxGoal = findViewById(R.id.edtMaxGoal)
        btnSaveGoals = findViewById(R.id.btnSaveGoals)

        db = AppDatabase.getDatabase(this)

        edtDate.setOnClickListener {
            showDatePicker()
        }

        btnPhoto.setOnClickListener {

        }

        btnSaveExpenses.setOnClickListener {
            saveExpense()
        }

        btnSaveGoals.setOnClickListener {
            saveGoals()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveExpense(){
        val category = edtCategory.text.toString().trim()
        val amountText = edtAmount.text.toString().trim()
        val date = edtDate.text.toString().trim()
        val description = edtDescription.text.toString().trim()

        //validation checks
        if(category.isEmpty() || amountText.isEmpty() || date.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()

        if(amount == null){
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            category = category,
            amount = amount,
            date = date,
            description = description,
            photoUri = selectedPhotoUri
        )

        lifecycleScope.launch{
            db.expenseDao().insertExpense(expense)

            runOnUiThread {
                Toast.makeText(this@Expenses, "Expense saved successfully", Toast.LENGTH_SHORT).show()

                edtCategory.text.clear()
                edtAmount.text.clear()
                edtDate.text.clear()
                edtDescription.text.clear()
                selectedPhotoUri = null
            }
        }
    }

    private fun saveGoals(){

        val minText = edtMinGoal.text.toString().trim()
        val maxText = edtMaxGoal.text.toString().trim()

        if(minText.isEmpty() || maxText.isEmpty()){
            Toast.makeText(this, "Please enter your minimum AND maximum goals", Toast.LENGTH_SHORT).show()
            return
        }

        val minGoal = minText.toDoubleOrNull()
        val maxGoal = maxText.toDoubleOrNull()

        if(minGoal == null || maxGoal ==null){
            Toast.makeText(this, "Please enter valid goal amounts", Toast.LENGTH_SHORT).show()
            return
        }

        if(minGoal > maxGoal){
            Toast.makeText(this, "Minimum goal cannot be greater than maximum goal", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch{
            val existingGoal = db.monthlyGoalDao().getGoal()

            if(existingGoal == null){
                val newGoal = MonthlyGoal(
                    minGoal = minGoal,
                    maxGoal = maxGoal
                )
                db.monthlyGoalDao().insertGoal(newGoal)
            }else{
                val updatedGoal = existingGoal.copy(
                    minGoal = minGoal,
                    maxGoal = maxGoal
                )
                db.monthlyGoalDao().updateGoal(updatedGoal)
            }

            runOnUiThread {
                Toast.makeText(this@Expenses, "Goals saved successfully", Toast.LENGTH_SHORT).show()
                edtMinGoal.text.clear()
                edtMaxGoal.text.clear()
            }
        }

    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            {_, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                edtDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()

    }

}