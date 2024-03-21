package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.utils.Utils

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // expand the navigation bottom bar
        binding.btnExpandNav.setOnClickListener {
            // hide keyboard first
            Utils.hideSoftKeyboard(this, it)
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.etChatMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.etChatMessage.text.isNotBlank()) {
                binding.btnSendMessage.visibility = View.VISIBLE
            } else {
                binding.btnSendMessage.visibility = View.GONE
            }
        }
    }
}