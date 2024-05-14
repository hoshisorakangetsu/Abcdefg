package com.example.abcdefg

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityCreateGroupBinding
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GroupCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var auth: FirebaseAuth

    // List to store selected chip names
    private val selectedChipNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.chipComputerScience.setOnClickListener {
            Toast.makeText(this, binding.chipComputerScience.text, Toast.LENGTH_SHORT).show()
        }

        entryChip()

        binding.btnCreateGroup.setOnClickListener {
            val db = Firebase.firestore

            val groupName = binding.inputGroupUsername.editText?.text.toString()
            val groupDescription = binding.inputGroupDescription.editText?.text.toString()

            if (groupName.isBlank() || groupDescription.isBlank()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val group = Group(
                name = groupName,
                desc = groupDescription,
                ownerUid = auth.uid ?: "",
                imgPath = "",
                memberUids = listOf(),
                tags = selectedChipNames // Include the selected chip names here
            )

            db.collection("groups")
                .add(group)
                .addOnSuccessListener {
                    Toast.makeText(this, "Group successfully created", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error creating group", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun entryChip() {
        binding.etName.setOnKeyListener { v, keycode, event ->
            if (keycode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.apply {
                    val name = etName.text.toString()
                    createChips(name)
                    etName.text.clear()
                }
                return@setOnKeyListener true
            }
            false
        }
        choiceChips()
    }

    private fun choiceChips() {
        binding.chipGroupInterest.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedChipNames.clear() // Clear the list to avoid duplicates
            for (id in checkedIds) {
                val chip: Chip? = group.findViewById(id)
                chip?.let {
                    selectedChipNames.add(it.text.toString())
                }
            }
            // Display the selected chip names (optional)
            Toast.makeText(this, selectedChipNames.joinToString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun createChips(name: String) {
        val chip = Chip(this)
        chip.apply {
            text = name
            // Apply style attributes
            setTextColor(ContextCompat.getColor(this@GroupCreateActivity, R.color.md_theme_light_primary))
            setCheckedIconVisible(false)
            chipBackgroundColor = ContextCompat.getColorStateList(this@GroupCreateActivity, R.color.chip_background_color)
            chipIcon = null // Hide chip icon
            isClickable = true
            isCheckable = true

            // Add the chip to the ChipGroup
            binding.chipGroupInterest.addView(this)

            // Set listener to remove chip when close icon is clicked
            setOnCloseIconClickListener {
                binding.chipGroupInterest.removeView(this)
                selectedChipNames.remove(this.text.toString()) // Remove from list if unchecked
            }
        }
    }
}