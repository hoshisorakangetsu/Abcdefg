package com.example.abcdefg

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityRegisterBinding
import com.example.abcdefg.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correct the capitalization of 'layoutInflater'
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        auth = Firebase.auth

        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            if (checkInputField()) {
                // input fields valid, execute signup
                auth.createUserWithEmailAndPassword(
                    binding.inputTxtUsername.text.toString(),
                    binding.inputTxtPassword.text.toString()
                ).addOnSuccessListener {
                    Toast.makeText(this, "Register successful, logged in automatically", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun checkInputField(): Boolean {
        var allFieldsEntered = true

        // Check each input field
        if (binding.inputTxtUsername.text.toString().isEmpty()) {
            binding.inputTxtUsername.error = "Please enter email"
            allFieldsEntered = false
        } else {
            binding.inputTxtUsername.error = null // Clear error if not empty
        }

        if (!Utils.isEmailValid(binding.inputTxtUsername.text.toString())) {
            binding.inputTxtUsername.error = "Invalid email format"
            allFieldsEntered = false
        } else {
            binding.inputTxtUsername.error = null // Clear error if not empty
        }

        if (binding.inputTxtPassword.text.toString().isEmpty()) {
            binding.inputTxtPassword.error = "Please enter password"
            allFieldsEntered = false
        } else {
            binding.inputTxtPassword.error = null // Clear error if not empty
        }

        if (binding.inputTxtConfirmPassword.text.toString().isEmpty()) {
            binding.inputTxtConfirmPassword.error = "Please enter confirm password"
            allFieldsEntered = false
            // Check if passwords match

        } else {
            binding.inputTxtConfirmPassword.error = null // Clear error if not empty
            val password = binding.inputTxtPassword.text.toString()
            val confirmPassword = binding.inputTxtConfirmPassword.text.toString()
            if (password != confirmPassword) {
                binding.inputTxtConfirmPassword.error = "Passwords do not match"
                allFieldsEntered = false
            } else {
                binding.inputTxtConfirmPassword.error = null // Clear error if passwords match
            }
        }

        return allFieldsEntered
    }

}
