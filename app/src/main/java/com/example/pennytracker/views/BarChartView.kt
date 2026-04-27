package com.example.pennytracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLUE
    }

    var data: List<Float> = listOf(100f, 200f, 150f, 300f, 250f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val barWidth = width / (data.size * 2)
        val maxValue = data.maxOrNull() ?: 1f

        data.forEachIndexed { index, value ->
            val left = index * barWidth * 2f
            val top = height - (value / maxValue * height)
            val right = left + barWidth
            val bottom = height.toFloat()

            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
}