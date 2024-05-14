package com.example.abcdefg

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.abcdefg.databinding.ActivityEditEventBinding
import com.example.abcdefg.databinding.FragmentSavedBlogsBinding

class EditEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra("eventId")!!
    }
}