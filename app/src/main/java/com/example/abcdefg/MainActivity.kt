package com.example.abcdefg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        // if alrd logged in then can proceed lo
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }
}