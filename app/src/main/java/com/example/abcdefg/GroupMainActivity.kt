package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityGroupMainBinding

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExpandBtm.setOnClickListener {
            binding.btmExpand.visibility = if (binding.btmExpand.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}