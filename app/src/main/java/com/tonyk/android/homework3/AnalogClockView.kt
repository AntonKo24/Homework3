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
    private var clockRadius = DEFAULT_CLOCK_RADIUS
    private var secondHandLength = DEFAULT_SECOND_HAND_LENGTH
    private var minuteHandLength = DEFAULT_MINUTE_HAND_LENGTH
    private var hourHandLength = DEFAULT_HOUR_HAND_LENGTH
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val scaleFactor = width.coerceAtMost(height) / DEFAULT_SIZE

        clockRadius = DEFAULT_CLOCK_RADIUS * scaleFactor
        secondHandLength = DEFAULT_SECOND_HAND_LENGTH * scaleFactor
        minuteHandLength = DEFAULT_MINUTE_HAND_LENGTH * scaleFactor
        hourHandLength = DEFAULT_HOUR_HAND_LENGTH * scaleFactor

        setMeasuredDimension((clockRadius * 2).toInt(), (clockRadius * 2).toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.apply {
            center.x = width.toFloat() / 2
            center.y = height.toFloat() / 2

            drawClockFace(this, clockRadius)
            drawClockHands(this, secondHandLength, minuteHandLength, hourHandLength)
        }
    }

    private fun drawClockFace(canvas: Canvas, clockRadius: Float) {
        val innerRadius = clockRadius - 20f
        canvas.drawCircle(center.x, center.y, innerRadius, paint)

        val tickAngles = DoubleArray(12) { Math.PI / 6 * it }
        val cosValues = FloatArray(12) { cos(tickAngles[it]).toFloat() }
        val sinValues = FloatArray(12) { sin(tickAngles[it]).toFloat() }

        for (i in 0..11) {
            val startX = center.x + (innerRadius - tickLength) * cosValues[i]
            val startY = center.y + (innerRadius - tickLength) * sinValues[i]
            val endX = center.x + innerRadius * cosValues[i]
            val endY = center.y + innerRadius * sinValues[i]
            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }

    private fun drawClockHands(canvas: Canvas, secondHandLength: Float, minuteHandLength: Float, hourHandLength: Float) {
        calendar.time = currentDate
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val secondAngle = Math.toRadians(360.0 * second / 60 - 90.0)
        val minuteAngle = Math.toRadians(360.0 * (minute + second / 60.0) / 60 - 90.0)
        val hourAngle = Math.toRadians(360.0 * (hour + minute / 60.0) / 12 - 90.0)

        val offset = 0.2f * secondHandLength

        val secondHandOffsetX = offset * cos(secondAngle).toFloat()
        val secondHandOffsetY = offset * sin(secondAngle).toFloat()

        val minuteHandOffsetX = offset * cos(minuteAngle).toFloat()
        val minuteHandOffsetY = offset * sin(minuteAngle).toFloat()

        val hourHandOffsetX = offset * cos(hourAngle).toFloat()
        val hourHandOffsetY = offset * sin(hourAngle).toFloat()

        drawHand(canvas, center.x - secondHandOffsetX, center.y - secondHandOffsetY, center.x + secondHandLength * cos(secondAngle).toFloat(), center.y + secondHandLength * sin(secondAngle).toFloat(), secondHandPaint)
        drawHand(canvas, center.x - minuteHandOffsetX, center.y - minuteHandOffsetY, center.x + minuteHandLength * cos(minuteAngle).toFloat(), center.y + minuteHandLength * sin(minuteAngle).toFloat(), minuteHandPaint)
        drawHand(canvas, center.x - hourHandOffsetX, center.y - hourHandOffsetY, center.x + hourHandLength * cos(hourAngle).toFloat(), center.y + hourHandLength * sin(hourAngle).toFloat(), hourHandPaint)
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

    private fun drawHand(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float, paint: Paint) {
        canvas.drawLine(startX, startY, endX, endY, paint)
    }

    companion object {
        private const val DEFAULT_SIZE = 600f
        private const val DEFAULT_CLOCK_RADIUS = 300f
        private const val DEFAULT_SECOND_HAND_LENGTH = 260f
        private const val DEFAULT_MINUTE_HAND_LENGTH = 230f
        private const val DEFAULT_HOUR_HAND_LENGTH = 200f
    }
}
