package com.example.abcdefg.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Blog
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentBlogCardBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class BlogListAdapter(var blogs: ArrayList<DocumentSnapshot>, private val blogClickedListener: BlogClickedListener) : RecyclerView.Adapter<BlogListAdapter.ViewHolder>() {

    fun interface BlogClickedListener {
        fun onBlogClicked(data: DocumentSnapshot)
    }

    class ViewHolder(private val binding: FragmentBlogCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(_data: DocumentSnapshot, blogClickedListener: BlogClickedListener) {
            val db = Firebase.firestore
            val data = _data.toObject(Blog::class.java)!!
            binding.tvBlogTitle.text = data.title
            binding.tvPostTime.text = SimpleDateFormat("dd MMM yyyy").format((data.createdAt as Timestamp).toDate())

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

            binding.cvBlogCard.setOnClickListener {
                blogClickedListener.onBlogClicked(_data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FragmentBlogCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blogs[position], blogClickedListener)
    }
}