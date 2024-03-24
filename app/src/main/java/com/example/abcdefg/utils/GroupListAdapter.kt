package com.example.abcdefg.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.R
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.FragmentGroupCardBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class GroupListAdapter(private val groups: ArrayList<Group>) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    class ViewHolder(private val binding: FragmentGroupCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Group) {
            binding.tvGroupName.text = data.name
            // bind will be called multiple times when it exits and reenters the view, clear out the chips first
            binding.cgInterestChip.removeAllViews()
            for (tag in data.tags) {
                val chip = createChip(tag)
                binding.cgInterestChip.addView(chip)
            }
        }

        private fun createChip(tag: String): Chip {
            val chip = Chip(binding.root.context)
            chip.text = tag
            val drawable =
                ChipDrawable.createFromAttributes(itemView.context, null, 0, R.style.PrimaryChip)
            chip.setChipDrawable(drawable)
            chip.isChipIconVisible = false
            chip.isCloseIconVisible = false
            chip.isCheckable = false
            chip.chipStrokeWidth = 0f
            chip.chipStrokeColor = null
            return chip
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentGroupCardBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groups[position])
    }
}