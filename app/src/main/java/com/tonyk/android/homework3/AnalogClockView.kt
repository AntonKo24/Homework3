package com.tonyk.android.homework3

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class AnalogClockView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val center = PointF(0f, 0f)
    private val clockRadius = 300f
    private val secondHandLength = 260f
    private val minuteHandLength = 230f
    private val hourHandLength = 200f
    private val tickLength = 20f
    private val tickPaint = Paint()
    private val secondHandPaint = Paint()
    private val minuteHandPaint = Paint()
    private val hourHandPaint = Paint()
    private val calendar = Calendar.getInstance()
    private var currentDate = Date()


    init {

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f

        tickPaint.style = Paint.Style.STROKE
        tickPaint.strokeWidth = 8f

        secondHandPaint.style = Paint.Style.STROKE
        secondHandPaint.strokeWidth = 8f

        minuteHandPaint.style = Paint.Style.STROKE
        minuteHandPaint.strokeWidth = 12f

        hourHandPaint.style = Paint.Style.STROKE
        hourHandPaint.strokeWidth = 18f

        applyAttributes(attrs)
        initializeTimeAnimation()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.apply {
            center.x = width.toFloat() / 2
            center.y = height.toFloat() / 2

            val scaleFactor = width.toFloat().coerceAtMost(height.toFloat()) / 600f

            val scaledClockRadius = clockRadius * scaleFactor
            val scaledSecondHandLength = secondHandLength * scaleFactor
            val scaledMinuteHandLength = minuteHandLength * scaleFactor
            val scaledHourHandLength = hourHandLength * scaleFactor

            drawClockFace(this, scaledClockRadius)
            drawClockHands(this, scaledSecondHandLength, scaledMinuteHandLength, scaledHourHandLength)

        }
    }

    private fun drawClockFace(canvas: Canvas, clockRadius: Float) {

        val innerRadius = clockRadius - 20f
        canvas.drawCircle(center.x, center.y, innerRadius, paint)

        for (i in 0..11) {
            val angle = Math.PI / 6 * i
            val startX = center.x + (innerRadius - tickLength) * cos(angle).toFloat()
            val startY = center.y + (innerRadius - tickLength) * sin(angle).toFloat()
            val endX = center.x + innerRadius * cos(angle).toFloat()
            val endY = center.y + innerRadius * sin(angle).toFloat()
            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }

    private fun drawClockHands(canvas: Canvas, secondHandLength: Float, minuteHandLength: Float, hourHandLength: Float) {
        calendar.time = currentDate
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val secondAngle = Math.PI * 2 * second / 60 - Math.PI / 2
        val minuteAngle = Math.PI * 2 * (minute + second / 60.0) / 60 - Math.PI / 2
        val hourAngle = Math.PI * 2 * (hour + minute / 60.0) / 12 - Math.PI / 2

        val offset = 0.2f * secondHandLength

        val secondHandStartX = center.x - offset * cos(secondAngle).toFloat()
        val secondHandStartY = center.y - offset * sin(secondAngle).toFloat()
        val secondHandEndX = center.x + secondHandLength * cos(secondAngle).toFloat()
        val secondHandEndY = center.y + secondHandLength * sin(secondAngle).toFloat()
        canvas.drawLine(secondHandStartX, secondHandStartY, secondHandEndX, secondHandEndY, secondHandPaint)

        val minuteHandStartX = center.x - offset * cos(minuteAngle).toFloat()
        val minuteHandStartY = center.y - offset * sin(minuteAngle).toFloat()
        val minuteHandEndX = center.x + minuteHandLength * cos(minuteAngle).toFloat()
        val minuteHandEndY = center.y + minuteHandLength * sin(minuteAngle).toFloat()
        canvas.drawLine(minuteHandStartX, minuteHandStartY, minuteHandEndX, minuteHandEndY, minuteHandPaint)

        val hourHandStartX = center.x - offset * cos(hourAngle).toFloat()
        val hourHandStartY = center.y - offset * sin(hourAngle).toFloat()
        val hourHandEndX = center.x + hourHandLength * cos(hourAngle).toFloat()
        val hourHandEndY = center.y + hourHandLength * sin(hourAngle).toFloat()
        canvas.drawLine(hourHandStartX, hourHandStartY, hourHandEndX, hourHandEndY, hourHandPaint)
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

    private fun applyAttributes(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView)
        val secondHandColor = typedArray.getColor(R.styleable.AnalogClockView_secondHandColor, Color.RED)
        val minuteHandColor = typedArray.getColor(R.styleable.AnalogClockView_minuteHandColor, Color.BLUE)
        val handColor = typedArray.getColor(R.styleable.AnalogClockView_hourHandColor, Color.BLACK)
        typedArray.recycle()

        secondHandPaint.color = secondHandColor
        minuteHandPaint.color = minuteHandColor
        hourHandPaint.color = handColor
    }

    private fun initializeTimeAnimation() {
        val timeAnimator = ValueAnimator.ofFloat(0f, 1f)
        timeAnimator.duration = 1000
        timeAnimator.repeatCount = ValueAnimator.INFINITE
        timeAnimator.addUpdateListener {
            currentDate = Date()
            invalidate()
        }
        timeAnimator.start()
    }
}
