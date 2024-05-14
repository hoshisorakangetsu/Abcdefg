package com.example.abcdefg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityCreateGroupBinding
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class GroupCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_STORAGE_PERMISSION = 2
    private lateinit var imageUrl: String
    <<<<<<< HEAD
    // List to store selected chip names
    private val selectedChipNames = mutableListOf<String>()

    =======
    >>>>>>> 5c0b0d2b7f6f287ce5ff0f0e3cca7209e2ca469b
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

            if (binding.inputGroupUsername.editText!!.text.isEmpty() || binding.inputGroupDescription.editText!!.text.isEmpty()){
                if (binding.inputGroupUsername.editText!!.text.isEmpty()){
                    binding.inputGroupUsername.editText!!.error = "Input required"
                }
                if (binding.inputGroupDescription.editText!!.text.isEmpty()){
                    binding.inputGroupDescription.editText!!.error= "Input required"
                }
                return@setOnClickListener
            }
            val group = Group(
                name = binding.inputGroupUsername.editText!!.text.toString(),
                desc = "" + binding.inputGroupDescription.editText!!.text.toString(),
                ownerUid = auth.uid!!,
                imgPath = imageUrl,
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
        binding.floatingAddPic.setOnClickListener{
            if (checkStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }
        <<<<<<< HEAD
    }
    =======
    >>>>>>> 5c0b0d2b7f6f287ce5ff0f0e3cca7209e2ca469b

}
private fun entryChip(){
    binding.etName.setOnKeyListener { v, keycode, event ->
        if (keycode == KeyEvent.KEYCODE_ENTER &&
            event.action == KeyEvent.ACTION_UP){
            ... (155 lines left)