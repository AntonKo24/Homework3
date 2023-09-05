package com.tonyk.android.homework3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class AnalogClockView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val center = PointF(0f, 0f)
    private var clockRadius = 300f
    private val secondHandLength = 260f
    private val minuteHandLength = 230f
    private val hourHandLength = 200f
    private val tickLength = 20f
    private val tickPaint = Paint()
    private val secondHandPaint = Paint()
    private val minuteHandPaint = Paint()
    private val hourHandPaint = Paint()
    private val textPaint = Paint()
    private val calendar = Calendar.getInstance()

    private val timeFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    private val currentDate = Date()


    init {

        tickPaint.style = Paint.Style.STROKE
        tickPaint.strokeWidth = 4f

        secondHandPaint.style = Paint.Style.STROKE
        secondHandPaint.strokeWidth = 8f

        minuteHandPaint.style = Paint.Style.STROKE
        minuteHandPaint.strokeWidth = 12f

        hourHandPaint.style = Paint.Style.STROKE
        hourHandPaint.strokeWidth = 16f

        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 40f

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                updateDate()
                invalidate()
                delay(1000)
            }
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView)
        val secondHandColor = typedArray.getColor(R.styleable.AnalogClockView_secondHandColor, Color.RED)
        val minuteHandColor = typedArray.getColor(R.styleable.AnalogClockView_minuteHandColor, Color.BLUE)
        val handColor = typedArray.getColor(R.styleable.AnalogClockView_hourHandColor, Color.BLACK)
        typedArray.recycle()

        secondHandPaint.color = secondHandColor
        minuteHandPaint.color = minuteHandColor
        hourHandPaint.color = handColor


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            center.x = width.toFloat() / 2
            center.y = height.toFloat() / 2

            val timeText = timeFormat.format(currentDate)


            drawClockFace(this)
            drawClockHands(this)
            drawTimeText(this, timeText)

        }
    }

    private fun drawClockFace(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        canvas.drawCircle(center.x, center.y, clockRadius, paint)

        for (i in 0..11) {
            val angle = Math.PI / 6 * i
            val startX = center.x + (clockRadius - tickLength) * cos(angle).toFloat()
            val startY = center.y + (clockRadius - tickLength) * sin(angle).toFloat()
            val endX = center.x + clockRadius * cos(angle).toFloat()
            val endY = center.y + clockRadius * sin(angle).toFloat()
            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }

    private fun drawClockHands(canvas: Canvas) {
        calendar.time = currentDate
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val secondAngle = Math.PI * 2 * second / 60 - Math.PI / 2
        val minuteAngle = Math.PI * 2 * (minute + second / 60.0) / 60 - Math.PI / 2
        val hourAngle = Math.PI * 2 * (hour + minute / 60.0) / 12 - Math.PI / 2

        val secondHandX = center.x + secondHandLength * Math.cos(secondAngle).toFloat()
        val secondHandY = center.y + secondHandLength * Math.sin(secondAngle).toFloat()
        canvas.drawLine(center.x, center.y, secondHandX, secondHandY, secondHandPaint)

        val minuteHandX = center.x + minuteHandLength * Math.cos(minuteAngle).toFloat()
        val minuteHandY = center.y + minuteHandLength * Math.sin(minuteAngle).toFloat()
        canvas.drawLine(center.x, center.y, minuteHandX, minuteHandY, minuteHandPaint)

        val hourHandX = center.x + hourHandLength * Math.cos(hourAngle).toFloat()
        val hourHandY = center.y + hourHandLength * Math.sin(hourAngle).toFloat()
        canvas.drawLine(center.x, center.y, hourHandX, hourHandY, hourHandPaint)
    }

    private fun drawTimeText(canvas: Canvas, timeText: String) {
        val textWidth = textPaint.measureText(timeText)
        val x = center.x - textWidth / 2
        val y = center.y + 100
        canvas.drawText(timeText, x, y, textPaint)
    }

    fun setSecondHandColor(color: Int) {
        secondHandPaint.color = color
        invalidate()
    }

    fun setMinuteHandColor(color: Int) {
        minuteHandPaint.color = color
        invalidate()
    }

    fun setHourHandColor(color: Int) {
        hourHandPaint.color = color
        invalidate()
    }

    private fun updateDate() {
        currentDate.time = System.currentTimeMillis()
    }
}










