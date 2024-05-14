package com.example.abcdefg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.ChatMessage
import com.example.abcdefg.data.Group
import com.example.abcdefg.data.Reply
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.databinding.FragmentGroupListNavBinding
import com.example.abcdefg.utils.Utils
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.viewmodels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

// TODO update selected group type (rn havent decide yet)
class GroupListDrawerAdapter(
    var groupList: ArrayList<DocumentSnapshot>,
    private val initialSelectedGroup: String,
    private val groupSelectedListener: GroupSelectedListener
) : RecyclerView.Adapter<GroupListDrawerAdapter.ViewHolder>() {

    fun interface GroupSelectedListener {
        fun onGroupSelected(group: DocumentSnapshot)
    }

    // change this as well
    var selectedGroup: String = this.initialSelectedGroup
        set(groupId) {
            notifyItemChanged(groupList.indexOfFirst { it.id == selectedGroup })
            field = groupId
            notifyItemChanged(groupList.indexOfFirst { it.id == groupId })
        }

    class ViewHolder(private val binding: FragmentGroupListNavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DocumentSnapshot, isSelected: Boolean, groupSelectedListener: GroupSelectedListener) {
            val group = data.toObject(Group::class.java)!!
            binding.tvGroupName.text = group.name
            if (group.imgPath.isNotBlank()) {
                Picasso.get().load(group.imgPath).into(binding.sivGroupPic)
            }
            binding.selectedOverlay.visibility = if (isSelected) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.mcvGroupDrawerItem.setOnClickListener {
                groupSelectedListener.onGroupSelected(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FragmentGroupListNavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("GroupMainActivity", groupList.indexOfFirst { it.id == selectedGroup }.toString())
        groupList.indexOfFirst {
            Log.d("GroupMainActivity", it.id)
            Log.d("GroupMainActivity", selectedGroup)
            it.id == selectedGroup
        }
        Log.d("GroupMainActivity", position.toString())
        holder.bind(
            groupList[position],
            groupList.indexOfFirst { it.id == selectedGroup } == position
        ) {
            selectedGroup = it.id
            groupSelectedListener.onGroupSelected(it)
        }
    }
}

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding

    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    private val groups: ArrayList<DocumentSnapshot> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup app bar
        setSupportActionBar(binding.topAppbar)

        // show the drawer icon on the app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val groupId = intent.getStringExtra("groupId")
        groupViewModel.navigateToNewGroup(groupId!!)

        auth = Firebase.auth
        val db = Firebase.firestore

        // get group reference
        db.collection("groups").document(groupId!!).get().addOnSuccessListener {
            supportActionBar?.title = it.toObject(Group::class.java)!!.name
        }

        fun toggleBtmNavVisibility() {
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                binding.btnExpandNav.animate().rotation(180f).setDuration(500).start()
                View.VISIBLE
            } else {
                binding.btnExpandNav.animate().rotation(0f).setDuration(500).start()
                View.GONE
            }
        }

        // setup navigation drawer
        val drawerLayout = binding.groupDrawer
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.topAppbar,
            R.string.open_drawer_group_list,
            R.string.close_drawer_group_list
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setupSideNav(db, groupId)

        binding.cvNavProfileCard.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        binding.llJoinedGroups.setOnClickListener {
            finish()
        }

        // bind bottom nav bar to nav controller
        val btmNavHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.btmNav.setupWithNavController(btmNavHostFrag.navController)
        binding.btmNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.groupChatFragment -> groupViewModel.navigateToNewFragment(GroupViewModel.Companion.GroupMainFragments.CHAT)
                R.id.groupTopicFragment -> groupViewModel.navigateToNewFragment(GroupViewModel.Companion.GroupMainFragments.TOPIC_LIST)
                R.id.groupEventFragment -> groupViewModel.navigateToNewFragment(GroupViewModel.Companion.GroupMainFragments.EVENT)
            }
            toggleBtmNavVisibility()
            btmNavHostFrag.navController.navigate(it.itemId)
            true
        }
        // FIXME not working if navigate to Topic -> Topic Content -> Chat / Event -> Topic
        binding.btmNav.setOnItemReselectedListener { item ->
            // Pop everything up to the reselected item
            val reselectedDestinationId = item.itemId
            btmNavHostFrag.navController.popBackStack(reselectedDestinationId, inclusive = false)
        }

        groupViewModel.activeFragmentType.observe(this) {
            // hide all controls first (except the first one which is the expand nav button
            for (i in 1..< binding.clBtmBar.childCount) {
                binding.clBtmBar.getChildAt(i)?.visibility = View.GONE
            }

            // hide all expanded search options ka etc
            for (i in 1..< binding.llBtm.childCount) {
                binding.llBtm.getChildAt(i)?.visibility = View.GONE
            }
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (it) {
                GroupViewModel.Companion.GroupMainFragments.CHAT -> binding.btmMessageLayout.visibility =
                    View.VISIBLE

                GroupViewModel.Companion.GroupMainFragments.TOPIC_LIST -> binding.btmTopicList.visibility = View.VISIBLE

                GroupViewModel.Companion.GroupMainFragments.TOPIC_CONTENT -> binding.btmMessageLayout.visibility =
                    View.VISIBLE

                GroupViewModel.Companion.GroupMainFragments.EVENT -> binding.btmEventList.visibility = View.VISIBLE

            }
        }

        // expand the navigation bottom bar
        binding.btnExpandNav.setOnClickListener {
            // hide keyboard first
            Utils.hideSoftKeyboard(this, it)
            toggleBtmNavVisibility()
        }

        setupChat(db)
        populateUserProfileData()

        binding.btnNewTopic.setOnClickListener {
            val i = Intent(this, CreateTopicActivity::class.java)
            i.putExtra("groupId", groupViewModel.activeGroupId.value)
            startActivity(i)
        }

        binding.btnNewEvent.setOnClickListener {
            val i = Intent(this, CreateEventActivity::class.java)
            i.putExtra("groupId", groupViewModel.activeGroupId.value)
            startActivity(i)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupSideNav(db: FirebaseFirestore, selectedGroupId: String) {
        val groupListAdapter = GroupListDrawerAdapter(groups, selectedGroupId) { data ->
            groupViewModel.navigateToNewGroup(data.id)
        }
        groupViewModel.activeGroupId.observe(this) {
            if (it.isNotBlank()) groupListAdapter.selectedGroup = it
        }
        binding.rvGroupListDrawer.adapter = groupListAdapter

        binding.btnJoinGroup.setOnClickListener {
            startActivity(Intent(this, GroupExploreActivity::class.java))
        }

        // fetch data for the first time
        db.collection("groups").where(
            Filter.or(
            Filter.arrayContains("memberUids", auth.uid),
            Filter.equalTo("ownerUid", auth.uid)
        )).get().addOnSuccessListener {
            groups.clear()
            it.documents.forEach { doc ->
                groups.add(doc)
            }
            groupListAdapter.groupList = groups
            groupListAdapter.notifyDataSetChanged()
        }
        // attach listener so it is updated everytime new group is created or groups are edited
        db.collection("groups").where(
            Filter.or(
            Filter.arrayContains("memberUids", auth.uid),
            Filter.equalTo("ownerUid", auth.uid)
        )).addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to group list change")
                return@addSnapshotListener
            }
            groups.clear()
            for (doc in value!!) {
                groups.add(doc)
            }
            groupListAdapter.groupList = groups
            groupListAdapter.notifyDataSetChanged()
        }
    }

    private fun setupChat(db: FirebaseFirestore) {
        binding.etMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.etMessage.text.isNotBlank()) {
                binding.btnSendMessage.visibility = View.VISIBLE
            } else {
                binding.btnSendMessage.visibility = View.GONE
            }
        }

        binding.btnSendMessage.setOnClickListener {
            if (binding.etMessage.text.toString().isBlank()) return@setOnClickListener

            // can only be one or the other
            if (groupViewModel.activeFragmentType.value == GroupViewModel.Companion.GroupMainFragments.CHAT)
                addToChat(binding.etMessage.text.toString(), auth.uid!!, db)
            else
                addToTopicReply(binding.etMessage.text.toString(), auth.uid!!, db)
        }
    }

    private fun addToChat(message: String, uid: String, db: FirebaseFirestore) {
        db.collection("chatMessages").add(ChatMessage(message, uid, groupViewModel.activeGroupId.value!!, Timestamp.now())).addOnSuccessListener {
            binding.etMessage.setText("");
        }
    }
    private fun addToTopicReply(reply: String, uid: String, db: FirebaseFirestore) {
        db.collection("topicReplies").add(Reply(reply, Timestamp.now(), uid, groupViewModel.activeTopicId.value!!)).addOnSuccessListener {
            binding.etMessage.setText("");
        }
    }

    private fun populateUserProfileData() {
        val db = Firebase.firestore

        db.collection("users").whereEqualTo("uid", auth.uid).addSnapshotListener { value, err ->
            if (err != null) {
                Log.d("EditUserProfile", "Cannot listen to snapshot changes")
                return@addSnapshotListener
            }
            binding.tvNavUsername.text = value!!.documents[0]?.get("name")!!.toString()
            if (value.documents[0]?.get("imagePath") != null && value.documents[0]?.get("imagePath")!!.toString().isNotBlank()) {
                Picasso.get().load(value.documents[0]?.get("imagePath")!!.toString()).into(binding.sivNavPfp)
            }
        }
    }
}