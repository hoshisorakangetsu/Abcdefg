package com.example.abcdefg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityLoginBinding
import com.example.abcdefg.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            if (binding.inputTxtPassword.text!!.isEmpty() || binding.inputTxtUsername.text!!.isEmpty()){
                if (binding.inputTxtUsername.text!!.isEmpty()){
                    binding.inputTxtUsername.error = "Input required"
                }
                if (binding.inputTxtPassword.text!!.isEmpty()){
                    binding.inputTxtPassword.error = "Input required"
                }
                return@setOnClickListener
            }
            val email = binding.inputTxtUsername.text.toString()
            val password = binding.inputTxtPassword.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputTxtUsername.error = "Invalid email format"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                email,
                binding.inputTxtPassword.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }.addOnFailureListener {
                Log.d("Cannot log in", it.toString(), it.cause)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }
}