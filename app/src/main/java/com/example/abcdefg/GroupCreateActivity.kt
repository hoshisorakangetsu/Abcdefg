package com.example.abcdefg

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityCreateGroupBinding
import com.example.abcdefg.databinding.ActivityLoginBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupCreateActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var button : FloatingActionButton
    private lateinit var binding: ActivityCreateGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnCreateGroup.setOnClickListener {
            finish()
        }
    }
}