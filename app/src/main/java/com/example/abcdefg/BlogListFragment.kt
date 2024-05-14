package com.example.abcdefg

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
import java.util.Date

class BlogListFragment : Fragment() {

    private lateinit var binding: FragmentBlogListBinding

    private var blogs: ArrayList<Blog> = arrayListOf()

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
            BlogContentFragment().show(parentFragmentManager, "showBlogContent")
        }
        binding.rvExploreBlogList.adapter = blogListAdapter
        binding.rvExploreBlogList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

}