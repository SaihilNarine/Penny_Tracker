package com.example.pennytracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pennytracker.views.BarChartView
import com.example.pennytracker.views.LineChartView

class Graphs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.activity_graphs, container, false)

        val barChart = view.findViewById<BarChartView>(R.id.barChart)
        val lineChart = view.findViewById<LineChartView>(R.id.lineChart)

        barChart.data = listOf(120f, 200f, 150f, 300f, 250f)
        lineChart.data = listOf(50f, 100f, 80f, 200f, 150f)

        return view
    }
}