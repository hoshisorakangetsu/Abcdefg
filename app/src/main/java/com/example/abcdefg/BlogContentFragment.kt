package com.example.abcdefg

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.abcdefg.data.Blog
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentBlogCardBinding
import com.example.abcdefg.databinding.FragmentBlogContentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class BlogContentFragment(private val blog: DocumentSnapshot) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBlogContentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogContentBinding.inflate(inflater, container, false)

        val db = Firebase.firestore

        val data = blog.toObject(Blog::class.java)!!
        binding.tvBlogTitle.text = data.title
        binding.tvBlogPostDate.text = SimpleDateFormat("dd MMM yyyy HH:mm").format((data.createdAt as Timestamp).toDate())
        binding.blogContent.text = data.content

        if (data.imgPath.isNotBlank()) {
            Picasso.get().load(data.imgPath).into(binding.sivBlogBg)
        }

        // fetch user
        db.collection("users").whereEqualTo("uid", data.createdBy).get().addOnSuccessListener {
            val user = it.documents[0]?.toObject(User::class.java)!!
            binding.tvUsername.text = user.name
            if (user.imagePath.isNotBlank()) {
                Picasso.get().load(user.imagePath).into(binding.sivPfp)
            }
        }

        if (data.savedBy.contains(auth.uid)) {
            binding.btnSaveBlog.isEnabled = false
        } else {
            binding.btnSaveBlog.setOnClickListener {
                db.collection("blogs").document(blog.id).update("savedBy", FieldValue.arrayUnion(auth.uid!!)).addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved blog", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }

        return binding.root
    }
}