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
    private val blogs: ArrayList<Blog> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBlogsBinding.inflate(inflater, container, false)

        val adapter = BlogListAdapter(blogs) {
            BlogContentFragment().show(parentFragmentManager, "showMyBlogContent")
        }
        binding.rvSavedBlogList.adapter = adapter
        binding.rvSavedBlogList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

}