package com.example.abcdefg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.FragmentJoinedGroupListBinding
import com.example.abcdefg.utils.GroupListAdapter
import com.example.abcdefg.utils.VerticalSpacingItemDecoration
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class JoinedGroupListFragment : Fragment() {
    private lateinit var binding: FragmentJoinedGroupListBinding
    private val groupList: ArrayList<Group> = arrayListOf()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinedGroupListBinding.inflate(inflater, container, false)

        // setup the groups
        val joinedGroupAdapter = GroupListAdapter(groupList) {
            startActivity(Intent(activity, GroupMainActivity::class.java))
        }
        val db = Firebase.firestore
        // fetch data for the first time
        db.collection("groups").whereArrayContains("memberUids", auth.uid.toString()).get().addOnSuccessListener {
            groupList.clear()
            it.documents.forEach { doc ->
                doc.toObject(Group::class.java)?.let { grp ->
                    groupList.add(grp)
                }
            }
            joinedGroupAdapter.groups = groupList
            joinedGroupAdapter.notifyDataSetChanged()
        }
        // attach listener so it is updated everytime new group is created or groups are edited
        db.collection("groups").addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to group list change")
                return@addSnapshotListener
            }
            groupList.clear()
            for (doc in value!!) {
                groupList.add(doc.toObject(Group::class.java))
            }
            joinedGroupAdapter.groups = groupList
            joinedGroupAdapter.notifyDataSetChanged()
        }
        binding.rvJoinedGroupList.adapter = joinedGroupAdapter
        binding.rvJoinedGroupList.addItemDecoration(VerticalSpacingItemDecoration(16))

        return binding.root
    }

    // TODO implement this
    private fun getData(): ArrayList<Group> {
        val res: ArrayList<Group> = arrayListOf()

        val db = Firebase.firestore

        db.collection("groups").get().addOnSuccessListener {
            it.documents.forEach { doc ->
                doc.toObject(Group::class.java)?.let { grp -> res.add(grp) }
            }
        }

        return res
    }

}