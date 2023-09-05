package com.tonyk.android.homework3

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tonyk.android.homework3.databinding.ActivityMainBinding
import yuku.ambilwarna.AmbilWarnaDialog


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var secondHandColorToPick = Color.RED
    private var minuteHandColorToPick = Color.GREEN
    private var hourHandColorToPick = Color.BLUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.pickSecondsColor.setOnClickListener {
            openColorPickerDialog(secondHandColorToPick)
        }

        binding.pickMinutesColor.setOnClickListener {
            openColorPickerDialog(minuteHandColorToPick)
        }

        binding.pickHoursColor.setOnClickListener {
            openColorPickerDialog(hourHandColorToPick)
        }
    }

    private fun openColorPickerDialog(initialColor: Int) {
        val colorPickerDialog = AmbilWarnaDialog(this, initialColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {
            }
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                when (initialColor) {
                    secondHandColorToPick -> {
                        secondHandColorToPick = color
                        binding.customClock.setSecondHandColor(secondHandColorToPick)
                    }
                    minuteHandColorToPick -> {
                        minuteHandColorToPick = color
                        binding.customClock.setMinuteHandColor(minuteHandColorToPick)
                    }
                    hourHandColorToPick -> {
                        hourHandColorToPick = color
                        binding.customClock.setHourHandColor(hourHandColorToPick)
                    }
                }
            }
        })
        colorPickerDialog.show()
    }
}


