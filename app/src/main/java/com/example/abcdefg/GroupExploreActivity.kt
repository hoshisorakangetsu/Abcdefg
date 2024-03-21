package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.databinding.ActivityGroupExploreBinding

class GroupExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupExploreBinding.inflate(layoutInflater)
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