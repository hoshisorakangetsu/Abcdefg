package com.example.abcdefg

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityCreateGroupBinding
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

        auth = Firebase.auth

        binding.btnCreateGroup.setOnClickListener {
            val db = Firebase.firestore

            val group = Group(
                name = binding.inputGroupName.editText!!.text.toString(),
                ownerUid = auth.uid!!,
                grpImgPath = "",
                memberUids = listOf(),
                tags = listOf(),
            )

            db.collection("groups")
                .add(group)
                .addOnSuccessListener { _ ->
                    Toast.makeText(this, "Group successfully created", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { _ ->
                    Toast.makeText(this, "Error creating group", Toast.LENGTH_SHORT).show()
                }

            finish()
        }
    }
}