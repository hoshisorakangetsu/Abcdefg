package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityGroupBinding

class GroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
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