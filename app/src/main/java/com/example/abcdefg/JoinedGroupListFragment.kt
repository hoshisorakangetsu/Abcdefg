package com.example.abcdefg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.FragmentJoinedGroupListBinding
import com.example.abcdefg.utils.GroupListAdapter
import com.example.abcdefg.utils.VerticalSpacingItemDecoration


class JoinedGroupListFragment : Fragment() {
    private lateinit var binding: FragmentJoinedGroupListBinding
    private lateinit var groupList: ArrayList<Group>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinedGroupListBinding.inflate(inflater, container, false)

        // setup the groups
        groupList = getData()
        val joinedGroupAdapter = GroupListAdapter(groupList) {
            startActivity(Intent(activity, GroupMainActivity::class.java))
        }
        binding.rvJoinedGroupList.adapter = joinedGroupAdapter
        binding.rvJoinedGroupList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    // TODO implement this
    private fun getData(): ArrayList<Group> {
        return ArrayList()
    }

}