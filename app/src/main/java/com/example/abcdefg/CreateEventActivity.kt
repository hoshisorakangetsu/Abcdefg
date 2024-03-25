package com.example.abcdefg

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.abcdefg.databinding.ActivityCreateEventBinding
import com.example.abcdefg.databinding.ActivityUserProfileBinding
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.utils.transformIntoTimePicker
import java.text.SimpleDateFormat
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)

        binding.etSelectDate.transformIntoDatePicker("dd MMM yyyy")
        binding.etStartTime.transformIntoTimePicker()
        binding.etEndTime.transformIntoTimePicker()
    }
}