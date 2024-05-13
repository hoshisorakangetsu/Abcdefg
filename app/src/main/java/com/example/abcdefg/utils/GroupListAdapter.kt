package com.example.abcdefg.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.R
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.FragmentGroupCardBinding
import com.example.abcdefg.utils.Utils.Companion.createChip
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.firebase.firestore.DocumentSnapshot

// need convert the document snapshot to Group class manually
class GroupListAdapter(var groups: ArrayList<DocumentSnapshot>, private val onGroupClickListener: OnGroupClickListener) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    fun interface OnGroupClickListener {
        fun onGroupClicked(groupDoc: DocumentSnapshot)
    }

    class ViewHolder(private val binding: FragmentGroupCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DocumentSnapshot, onGroupClickListener: OnGroupClickListener) {
            data.toObject(Group::class.java).let {
                binding.tvGroupName.text = it?.name
                binding.tvGroupDesc.text = it?.desc
                // bind will be called multiple times when it exits and reenters the view, clear out the chips first
                binding.cgInterestChip.removeAllViews()
                for (tag in it?.tags!!) {
                    val chip = createChip(itemView.context, tag)
                    binding.cgInterestChip.addView(chip)
                }
            }
            binding.root.setOnClickListener {
                onGroupClickListener.onGroupClicked(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentGroupCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groups[position], onGroupClickListener)
    }
}