package com.example.pennytracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LineChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    var data: List<Float> = listOf(50f, 100f, 80f, 200f, 150f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.size < 2) return

        val maxValue = data.maxOrNull() ?: 1f
        val stepX = width / (data.size - 1).toFloat()

        for (i in 0 until data.size - 1) {
            val x1 = i * stepX
            val y1 = height - (data[i] / maxValue * height)

            val x2 = (i + 1) * stepX
            val y2 = height - (data[i + 1] / maxValue * height)

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}