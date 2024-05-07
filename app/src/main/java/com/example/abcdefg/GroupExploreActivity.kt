package com.example.abcdefg

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.data.Group
import com.example.abcdefg.data.getAvailableInterestTags
import com.example.abcdefg.databinding.ActivityGroupExploreBinding
import com.example.abcdefg.utils.GroupListAdapter
import com.example.abcdefg.utils.Utils
import com.example.abcdefg.utils.VerticalSpacingItemDecoration

class GroupExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupExploreBinding

    private lateinit var groups: ArrayList<Group>

    private lateinit var interestTags: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
        }

        binding.ibClose.setOnClickListener {
            finish()
        }

        // populate interest tag chips
        interestTags = getAvailableInterestTags()
        for (t in interestTags) {
            val chip = Utils.createSelectableChip(this, t)
            binding.cgFilterChips.addView(chip)
        }

        // populate recycler view
        groups = getData()
        val exploreGroupAdapter = GroupListAdapter(groups) {
            // TODO view group details
        }
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

    override fun onPause() {
        overridePendingTransition(0, R.anim.slide_out_from_top)
        super.onPause()
    }
    override fun onDestroy() {
        overridePendingTransition(0, R.anim.slide_out_from_top)
        super.onDestroy()
    }

    // TODO implement this
    private fun getData(): ArrayList<Group> {
        return ArrayList()
    }
}