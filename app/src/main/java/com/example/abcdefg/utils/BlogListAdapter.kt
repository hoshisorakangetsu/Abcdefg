package com.example.abcdefg.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Blog
import com.example.abcdefg.databinding.FragmentBlogCardBinding

class BlogListAdapter(private val blogs: ArrayList<Blog>, private val blogClickedListener: BlogClickedListener) : RecyclerView.Adapter<BlogListAdapter.ViewHolder>() {

    fun interface BlogClickedListener {
        fun onBlogClicked(data: Blog)
    }

    class ViewHolder(private val binding: FragmentBlogCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Blog, blogClickedListener: BlogClickedListener) {
            binding.tvBlogTitle.text = data.title
            binding.cgTopicInterestChip.removeAllViews()
            for (tag in data.interestTags) {
                val chip = Utils.createChip(itemView.context, tag)
                binding.cgTopicInterestChip.addView(chip)
            }

            binding.cvBlogCard.setOnClickListener {
                blogClickedListener.onBlogClicked(data)
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