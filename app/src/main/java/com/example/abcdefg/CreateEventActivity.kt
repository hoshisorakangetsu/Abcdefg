package com.example.abcdefg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.FirestoreDateTimeFormatter
import com.example.abcdefg.databinding.ActivityCreateEventBinding
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.utils.transformIntoTimePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat

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

        @Suppress("SimpleDateFormat")
        binding.btnCreate.setOnClickListener {
            Log.d("CreateEventActivityStartTime", binding.etStartTime.text.toString())
            Log.d("CreateEventActivityStartTime", SimpleDateFormat("hh:mm a").parse(
                binding.etStartTime.text.toString()
            )!!.toString())
            Log.d("CreateEventActivityStartTime", FirestoreDateTimeFormatter.TimeFormatter.format(
                SimpleDateFormat("hh:mm a").parse(
                    binding.etStartTime.text.toString()
                )!!
            ))
            Log.d("CreateEventActivityEndTime", binding.etEndTime.text.toString())
            Log.d("CreateEventActivityEndTime", SimpleDateFormat("hh:mm a").parse(
                binding.etEndTime.text.toString()
            )!!.toString())
            Log.d("CreateEventActivityEndTime", FirestoreDateTimeFormatter.TimeFormatter.format(
                SimpleDateFormat("KK:mm a").parse(
                    binding.etEndTime.text.toString()
                )!!
            ))
            Log.d("CreateEventActivityDate", binding.etSelectDate.text.toString())
            Log.d("CreateEventActivityDate", SimpleDateFormat("dd MMM yyyy").parse(
                binding.etSelectDate.text.toString()
            )!!.toString())
            val e = Event(
                name = binding.inputEventTitle.text.toString(),
                description = binding.inputEventDescr.text.toString(),
                eventDate = FirestoreDateTimeFormatter.DateFormatter.format(
                    SimpleDateFormat("dd MMM yyyy").parse(
                        binding.etSelectDate.text.toString()
                    )!!
                ),
                eventStartTime = FirestoreDateTimeFormatter.TimeFormatter.format(
                    SimpleDateFormat("KK:mm a").parse(
                        binding.etStartTime.text.toString()
                    )!!
                ),
                eventEndTime = FirestoreDateTimeFormatter.TimeFormatter.format(
                    SimpleDateFormat("KK:mm a").parse(
                        binding.etEndTime.text.toString()
                    )!!
                ),
                groupId = groupId!!,
                createdBy = auth.uid!!
            )
            db.collection("events").add(e).addOnSuccessListener {
                finish()
            }
        }
    }
}