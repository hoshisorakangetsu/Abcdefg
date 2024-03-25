package com.example.abcdefg

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
import java.util.Date

class SavedBlogsFragment : Fragment() {

    private lateinit var binding: FragmentSavedBlogsBinding
    private lateinit var blogs: ArrayList<Blog>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBlogsBinding.inflate(inflater, container, false)

        blogs = getData()
        val adapter = BlogListAdapter(blogs) {
            BlogContentFragment().show(parentFragmentManager, "showMyBlogContent")
        }
        binding.rvSavedBlogList.adapter = adapter
        binding.rvSavedBlogList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    // TODO implement this
    private fun getData(): ArrayList<Blog> {
        val users = arrayOf(
            User("1", "Alice"),
            User("2", "Bob"),
            User("3", "Charlie")
        )

        val interests = arrayOf(
            "Technology",
            "Science",
            "Programming",
            "Art",
            "Travel",
            "Food"
        )

        val blogs = ArrayList<Blog>()

        for (i in 1..10) {
            val user = users.random()
            val createdAt = Date()
            val title = "Blog Title $i"
            val content = "Content of Blog $i"
            val interestTags = Array((1..3).random()) { interests.random() }

            val blog = Blog(title, content, interestTags, createdAt, user)
            blogs.add(blog)
        }

        return blogs
    }
}