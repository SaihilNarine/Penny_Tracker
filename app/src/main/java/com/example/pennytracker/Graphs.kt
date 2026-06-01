package com.example.pennytracker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import data.ChartData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*

class Graphs : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    //Filter
    private lateinit var edtFilterDate : EditText
    private lateinit var btnFilter : Button

    private var selectedDate = ""

    private val dbRef =
        FirebaseDatabase.getInstance()
            .getReference("chartData")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        //pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)
        pieChart = findViewById(R.id.pieChart)

        //Filter typecasting
        edtFilterDate = findViewById(R.id.edtFilterDate)
        btnFilter = findViewById(R.id.btnFilter)

        pieChart.setNoDataText("Loading data...")
        barChart.setNoDataText("Loading data...")

        loadData()

        //Date picker
        edtFilterDate.setOnClickListener {
            showDatePicker()
        }

        btnFilter.setOnClickListener {
            loadData(selectedDate)
        }
    }

    private fun loadData(filterDate: String = "") {

        dbRef.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {
                    val pieEntries = ArrayList<PieEntry>()

                    val barEntries = ArrayList<BarEntry>()

                    val labels = ArrayList<String>()

                    var index = 0f

                    for (child in snapshot.children) {

                        val item =
                            child.getValue(
                                ChartData::class.java
                            )

                        if (item != null) {

                            if (filterDate.isEmpty() || item.date == filterDate) {


                                pieEntries.add(
                                    PieEntry(
                                        item.value,
                                        item.category
                                    )
                                )

                                barEntries.add(
                                    BarEntry(
                                        index,
                                        item.value
                                    )
                                )

                                labels.add(item.category)

                                index++
                            }
                        }
                    }

                    setupPieChart(pieEntries)
                    setupBarChart(
                        barEntries,
                        labels
                    )
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {
                }
            }
        )
    }

    private fun setupPieChart(entries: List<PieEntry>) {

        val dataSet = PieDataSet(entries, "Expenses")

        dataSet.colors = listOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.CYAN
        )

        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.centerText = "Expenses"
        pieChart.animateY(1000)

        pieChart.invalidate()
    }

    private fun setupBarChart(entries: List<BarEntry>, labels: List<String>) {

        val dataSet = BarDataSet(entries, "Expenses")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)

        barChart.data = data

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false

        barChart.animateY(1000)
        barChart.invalidate()
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker(){
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            {_, year, month, day -> selectedDate =
                String.format(
                    "%02d/%02d/%04d",
                    day,
                    month + 1,
                    year
                )

                edtFilterDate.setText(selectedDate)
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}