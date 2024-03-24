package com.example.abcdefg

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityGroupExploreBinding
import com.example.abcdefg.utils.GroupListAdapter
import com.example.abcdefg.utils.VerticalSpacingItemDecoration

class GroupExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupExploreBinding

    private lateinit var groups: ArrayList<Group>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_from_top)
        }

        // populate recycler view
        groups = getData()
        val exploreGroupAdapter = GroupListAdapter(groups)
        binding.rvExploreGroupList.adapter = exploreGroupAdapter
        binding.rvExploreGroupList.addItemDecoration(VerticalSpacingItemDecoration(16))

        binding.btnExpandBtm.setOnClickListener {
            binding.btmExpand.visibility = if (binding.btmExpand.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
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