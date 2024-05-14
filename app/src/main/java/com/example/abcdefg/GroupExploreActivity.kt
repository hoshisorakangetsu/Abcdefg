package com.example.abcdefg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityGroupExploreBinding
import com.example.abcdefg.utils.GroupListAdapter
import com.example.abcdefg.utils.Utils
import com.example.abcdefg.utils.VerticalSpacingItemDecoration
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class GroupExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupExploreBinding

    private var groupList: ArrayList<DocumentSnapshot> = arrayListOf()

    private lateinit var interestTags: ArrayList<String>
    private lateinit var auth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
        }

        binding.ibClose.setOnClickListener {
            finish()
        }

        // populate recycler view
        val exploreGroupAdapter = GroupListAdapter(groupList) {
            val i = Intent(this, ViewGroupDetailActivity::class.java)
            i.putExtra("groupId", it.id)
            startActivity(i)
        }
        val db = Firebase.firestore
        // fetch data for the first time
        db.collection("groups").get().addOnSuccessListener {
            groupList.clear()
            it.documents.forEach {doc ->
                doc.toObject(Group::class.java)?.let { grp ->
                    if (!grp.memberUids.contains(auth.uid) && grp.ownerUid != auth.uid) {
                        groupList.add(doc)
                        Log.d("GroupExploreActivity", groupList.count().toString())
                    }
                }
            }
            exploreGroupAdapter.groups = groupList
            exploreGroupAdapter.notifyDataSetChanged()
        }

        // attach listener so it is updated everytime new group is created or groups are edited
        db.collection("groups").addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to group list change")
                return@addSnapshotListener
            }
            groupList.clear()
            value!!.documents.forEach { doc ->
                doc.toObject(Group::class.java)?.let { grp ->
                    if (!grp.memberUids.contains(auth.uid) && grp.ownerUid != auth.uid) {
                        groupList.add(doc)
                        Log.d("GroupExploreActivity", groupList.count().toString())
                    }
                }
            }
            exploreGroupAdapter.groups = groupList
            exploreGroupAdapter.notifyDataSetChanged()
        }
        binding.rvExploreGroupList.adapter = exploreGroupAdapter
        binding.rvExploreGroupList.addItemDecoration(VerticalSpacingItemDecoration(16))
    }

    override fun onPause() {
        overridePendingTransition(0, R.anim.slide_out_from_top)
        super.onPause()
    }
    override fun onDestroy() {
        overridePendingTransition(0, R.anim.slide_out_from_top)
        super.onDestroy()
    }
}