package com.example.abcdefg

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abcdefg.data.Blog
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentBlogListBinding
import com.example.abcdefg.utils.BlogListAdapter
import com.example.abcdefg.utils.VerticalSpacingItemDecoration
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document
import java.util.Date

class BlogListFragment : Fragment() {

    private lateinit var binding: FragmentBlogListBinding

    private var blogs: ArrayList<DocumentSnapshot> = arrayListOf()
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogListBinding.inflate(inflater, container, false)

        // populate recycler view
        val blogListAdapter = BlogListAdapter(blogs) {
            BlogContentFragment(it).show(parentFragmentManager, "showBlogContent")
        }
        populateData(blogListAdapter)
        binding.rvExploreBlogList.adapter = blogListAdapter
        binding.rvExploreBlogList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun populateData(adapter: BlogListAdapter) {
        val db = Firebase.firestore
        db.collection("blogs").get().addOnSuccessListener {
            blogs.clear()
            it.documents.forEach { doc ->
                blogs.add(doc)
            }
            adapter.blogs = blogs
            adapter.notifyDataSetChanged()
        }
        listener = db.collection("blogs").addSnapshotListener { value, error ->
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