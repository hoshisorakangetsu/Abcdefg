package com.example.abcdefg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.ActivityCreateBlogBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CreateBlogActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateBlogBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityCreateBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnPost.setOnClickListener(){
            val title:String=binding.tidtPostTitleInput.text.toString()
            val content:String=binding.tidtBlogContentInput.text.toString();
            insertBlog(title,content)
        }

    }
    private fun insertBlog(title: String,content: String) {
        val blog = User(title, auth.uid!!, content)
        val db = Firebase.firestore
        db.collection("blogs").add(blog).addOnSuccessListener {
            Toast.makeText(this, "Blog posted successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

}