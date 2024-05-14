package com.example.abcdefg

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.abcdefg.data.Event
import com.example.abcdefg.databinding.ActivityCreateEventBinding
import com.example.abcdefg.databinding.ActivityUserProfileBinding
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.utils.transformIntoTimePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getStringExtra("groupId")

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.etSelectDate.transformIntoDatePicker("dd MMM yyyy")
        binding.etStartTime.transformIntoTimePicker()
        binding.etEndTime.transformIntoTimePicker()

        binding.btnCreate.setOnClickListener {
            val e = Event(
                name = binding.txtEventTitle.text.toString(),
                description = binding.txtEventDescr.text.toString(),
                eventDate = Date(binding.txtEventDate.text.toString()),
                eventStartTime = Date(binding.txtEventStartTime.text.toString()),
                eventEndTime = Date(binding.txtEventEndTime.text.toString()),
                groupId = groupId!!
            )
            db.collection("events").add(e).addOnSuccessListener {
                finish()
            }
        }
    }
}