package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.abcdefg.databinding.ActivityGroupMainBinding

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExpandNav.setOnClickListener {
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}