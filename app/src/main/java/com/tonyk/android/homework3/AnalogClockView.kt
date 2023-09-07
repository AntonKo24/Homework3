package com.tonyk.android.homework3

import android.animation.ValueAnimator
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
    private var currentDate = Date()


    init {

        val timeAnimator = ValueAnimator.ofFloat(0f, 1f)
        timeAnimator.duration = 1000
        timeAnimator.repeatCount = ValueAnimator.INFINITE
        timeAnimator.addUpdateListener { animation ->
            currentDate = Date()
            invalidate()
        }
        timeAnimator.start()



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
        if (canvas == null) return

        canvas.apply {
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

}










