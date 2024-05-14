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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.chipComputerScience.setOnClickListener(){
            Toast.makeText(this,binding.chipComputerScience.text,Toast.LENGTH_SHORT).show()
        }

        entryChip()

        auth = Firebase.auth

        binding.btnCreateGroup.setOnClickListener {
            val db = Firebase.firestore

            val group = Group(
                name = binding.inputGroupUsername.editText!!.text.toString(),
                desc = "" + binding.inputGroupDescription.editText!!.text.toString(),
                ownerUid = auth.uid!!,
                grpImgPath = "",
                memberUids = listOf(),
                tags = listOf(),
            )

            db.collection("groups")
                .add(group)
                .addOnSuccessListener { _ ->
                    Toast.makeText(this, "Group successfully created", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { _ ->
                    Toast.makeText(this, "Error creating group", Toast.LENGTH_SHORT).show()
                }
        }


    }
    private fun entryChip(){
        binding.etName.setOnKeyListener { v, keycode, event ->
            if (keycode == KeyEvent.KEYCODE_ENTER &&
                event.action == KeyEvent.ACTION_UP){

                binding.apply{
                    val name = etName.text.toString()
                    createChips(name)
                    etName.text.clear()
                }

                return@setOnKeyListener true
            }

            false
        }


    }
    private  fun createChips(name:String){
        val chip = Chip(this)
        chip.apply{
            text = name
            chipIcon = ContextCompat.getDrawable(
                    this@GroupCreateActivity,
                    R.drawable.ct_close
                    )
            isChipIconVisible = false
            isCloseIconVisible = false
            isClickable = true
            isCheckable = true
            binding.apply {
                chipGroupInterest.addView(chip as View)
                chip.setOnCloseIconClickListener{
                    chipGroupInterest.removeView(chip as View)
                }
            }

        }
    }
}