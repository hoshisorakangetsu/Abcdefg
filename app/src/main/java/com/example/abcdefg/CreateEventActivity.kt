package com.example.abcdefg

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.example.abcdefg.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    lateinit var eventDate: TextView
    lateinit var btnDate: Button
    private val calender = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        //date variable
        eventDate = findViewById<TextView>(R.id.outputEventDate)
        btnDate = findViewById<Button>(R.id.btnSelectDate)

        //start time variable
        val btnStartTime = findViewById<Button>(R.id.btnStartTime)
        val eventStartTime = findViewById<TextView>(R.id.outputStartTime)

        //end time variable
        val btnEndTime = findViewById<Button>(R.id.btnEndTime)
        val eventEndTime = findViewById<TextView>(R.id.outputEndTime)

        btnDate.setOnClickListener() {
            showDatePicker()
        }

        btnStartTime.setOnClickListener(){
            val cal = Calendar.getInstance()
            val timeSetListner = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val formattedTime = SimpleDateFormat("HH:mm").format(cal.time)
                eventStartTime.text = formattedTime
            }
            TimePickerDialog(this, timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        btnEndTime.setOnClickListener(){
            val cal = Calendar.getInstance()
            val timeSetListner = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val formattedTime = SimpleDateFormat("HH:mm").format(cal.time)
                eventEndTime.text = formattedTime
            }
            TimePickerDialog(this, timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }

    private fun showTimePicker() {

    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            eventDate.text = formattedDate
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}