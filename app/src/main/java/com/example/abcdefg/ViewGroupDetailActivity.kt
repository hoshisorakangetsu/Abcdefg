package com.example.abcdefg

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityViewGroupDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class ViewGroupDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityViewGroupDetailBinding
    private lateinit var Auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewGroupDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Auth = Firebase.auth

        val group = intent.getStringExtra("groupId")!!
        val db = Firebase.firestore
        db.collection("groups").document(group).get().addOnSuccessListener {
            it.toObject(Group::class.java).let { grp ->
                grp!!.name
                grp!!.desc
                grp!!.tags
                grp!!.imgPath
                grp!!.memberUids

                binding.txtGroupName.text = grp.name
                binding.txtDescription.text = grp.desc

            }
        }
        binding.btnCreateGroup.setOnClickListener{ _ ->
            db.collection("groups").document(group).update("memberUids", FieldValue.arrayUnion(Auth.uid)).addOnSuccessListener {
                finish()
            }
        }


    }
}