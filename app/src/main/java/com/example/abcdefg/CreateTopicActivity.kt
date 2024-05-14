package com.example.abcdefg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abcdefg.data.Topic
import com.example.abcdefg.databinding.ActivityCreateGroupBinding
import com.example.abcdefg.databinding.ActivityCreateTopicBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CreateTopicActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCreateTopicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val db = Firebase.firestore
        val groupId = intent.getStringExtra("groupId")

        binding.ibClose.setOnClickListener {
            finish()
        }

        binding.btnPost.setOnClickListener {
            val topic = Topic(binding.tidtPostTitleInput.text.toString(), binding.tidtPostDescriptionInput.text.toString(), Timestamp.now(), auth.uid!!, groupId!!)
            db.collection("groupTopics").add(topic).addOnSuccessListener {
                finish()
            }
        }
    }
}