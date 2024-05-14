package com.example.abcdefg

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abcdefg.data.Blog
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentSavedBlogsBinding
import com.example.abcdefg.utils.BlogListAdapter
import com.example.abcdefg.utils.VerticalSpacingItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.util.Date

class SavedBlogsFragment : Fragment() {

    private lateinit var binding: FragmentSavedBlogsBinding
    private val blogs: ArrayList<DocumentSnapshot> = arrayListOf()
    private var listener: ListenerRegistration? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBlogsBinding.inflate(inflater, container, false)

        val adapter = BlogListAdapter(blogs) {
            BlogContentFragment(it).show(parentFragmentManager, "showMyBlogContent")
        }
        binding.rvSavedBlogList.adapter = adapter
        populateData(adapter)
        binding.rvSavedBlogList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun populateData(adapter: BlogListAdapter) {
        val db = Firebase.firestore
        db.collection("blogs").whereArrayContains("savedBy", auth.uid!!).get().addOnSuccessListener {
            blogs.clear()
            it.documents.forEach { doc ->
                blogs.add(doc)
            }
            adapter.blogs = blogs
            adapter.notifyDataSetChanged()
        }
        listener = db.collection("blogs").whereArrayContains("savedBy", auth.uid!!).addSnapshotListener { value, error ->
            if (error != null) {
                // smtg bad happened, cant listen to snapshot changes
                return@addSnapshotListener
            }
            blogs.clear()
            value!!.documents.forEach { doc ->
                blogs.add(doc)
            }
            adapter.blogs = blogs
            adapter.notifyDataSetChanged()
        }
    }
}