package com.example.abcdefg

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityHomeBinding
import com.example.abcdefg.databinding.FragmentGroupCardBinding
import com.example.abcdefg.databinding.FragmentJoinedGroupListBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class JoinedGroupAdapter(private val joinedGroups: ArrayList<Group>) : RecyclerView.Adapter<JoinedGroupAdapter.ViewHolder>() {
    class ViewHolder(private val binding: FragmentGroupCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Group) {
            binding.tvGroupName.text = data.name
            for (tag in data.tags) {
                val chip = createChip(tag)
                binding.cgInterestChip.addView(chip)
            }
        }

        private fun createChip(tag: String): Chip {
            val displayDensity = itemView.resources.displayMetrics.density
            val padding = (4 * displayDensity + 0.5f).toInt()
            val chip = Chip(binding.root.context)
            chip.text = tag
            val drawable = ChipDrawable.createFromAttributes(itemView.context, null, 0, R.style.PrimaryChip)
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
        return joinedGroups.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentGroupCardBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(joinedGroups[position])
    }
}

class VerticalSpacingItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            val displayDensity = view.resources.displayMetrics.density
            val spacing = (spaceSize * displayDensity + 0.5f).toInt()

            if (parent.getChildAdapterPosition(view) != 0) {
                top = spacing
            }
            left = spaceSize
            right = spaceSize
            bottom = spaceSize
        }
    }
}

class JoinedGroupListFragment : Fragment() {
    private lateinit var binding: FragmentJoinedGroupListBinding
    private lateinit var groupList: ArrayList<Group>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinedGroupListBinding.inflate(inflater, container, false)

        // setup the groups
        groupList = getData()
        val joinedGroupAdapter = JoinedGroupAdapter(groupList)
        binding.rvJoinedGroupList.adapter = joinedGroupAdapter
        binding.rvJoinedGroupList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    // TODO implement this
    private fun getData(): ArrayList<Group> {
        return ArrayList<Group>().apply {
            for (i in 0..10) {
                add(Group("Study Group $i", arrayOf("IT", "C++", "Java", "Android", "Kotlin")))
            }
        }
    }

}