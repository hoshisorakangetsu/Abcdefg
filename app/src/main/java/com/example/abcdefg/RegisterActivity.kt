package com.example.abcdefg

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.abcdefg.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correct the capitalization of 'layoutInflater'
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener{(checkInputField())}
    }

    fun checkInputField(): Boolean {
        var allFieldsEntered = true

        // Check each input field
        if (binding.inputTxtUsername.text.toString().isEmpty()) {
            binding.inputTxtUsername.error = "Please enter username"
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
            val password = binding.inputPassword.editText.toString()
            val confirmPassword = binding.inputConfirmPassword.editText.toString()
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
