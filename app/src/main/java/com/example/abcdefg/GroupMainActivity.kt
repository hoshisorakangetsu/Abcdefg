package com.example.abcdefg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Group
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.databinding.FragmentGroupListNavBinding
import com.example.abcdefg.utils.Utils
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.viewmodels.GroupViewModel

// TODO update selected group type (rn havent decide yet)
class GroupListDrawerAdapter(
    private val groupList: ArrayList<Group>,
    private val initialSelectedGroup: String,
    private val groupSelectedListener: GroupSelectedListener
) : RecyclerView.Adapter<GroupListDrawerAdapter.ViewHolder>() {

    fun interface GroupSelectedListener {
        fun onGroupSelected(group: Group)
    }

    // change this as well
    var selectedGroup: String = this.initialSelectedGroup
        set(groupName) {
            notifyItemChanged(groupList.indexOfFirst { it.name == selectedGroup })
            field = groupName
            notifyItemChanged(groupList.indexOfFirst { it.name == groupName })
        }

    class ViewHolder(private val binding: FragmentGroupListNavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Group, isSelected: Boolean, groupSelectedListener: GroupSelectedListener) {
            binding.tvGroupName.text = data.name
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
        holder.bind(groupList[position],
            groupList.indexOfFirst { it.name == selectedGroup } == position) {
            selectedGroup = it.name
            groupSelectedListener.onGroupSelected(it)
        }
    }
}

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding

    private val groupViewModel: GroupViewModel by viewModels()

    // TODO change this maybe put in view model
    private lateinit var groups: ArrayList<Group>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun toggleBtmNavVisibility() {
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                binding.btnExpandNav.animate().rotation(180f).setDuration(500).start()
                View.VISIBLE
            } else {
                binding.btnExpandNav.animate().rotation(0f).setDuration(500).start()
                View.GONE
            }
        }

        // setup app bar
        setSupportActionBar(binding.topAppbar)
        // TODO make this title dynamic
        supportActionBar?.title = "Study Group 1"

        // show the drawer icon on the app bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

        // populate group list in navigation drawer
        groups = getData()
        val groupListAdapter = groupViewModel.activeGroupId.value?.let {
            GroupListDrawerAdapter(groups, it) { data ->
                groupViewModel.navigateToNewGroup(data.name)
            }
        }
        binding.rvGroupListDrawer.adapter = groupListAdapter

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

        binding.etMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.etMessage.text.isNotBlank()) {
                binding.btnSendMessage.visibility = View.VISIBLE
            } else {
                binding.btnSendMessage.visibility = View.GONE
            }
        }

        // add toggle search dialog for topic
        binding.btnAdvancedSearchTopic.setOnClickListener {
            binding.filterTopic.visibility = if (binding.filterTopic.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // add toggle search dialog for event
        binding.btnAdvancedSearchEvent.setOnClickListener {
            binding.filterEvent.visibility = if (binding.filterEvent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // start date and end date edittext config
        binding.etStartDateTopic.transformIntoDatePicker("dd MMM yyyy")
        binding.etEndDateTopic.transformIntoDatePicker("dd MMM yyyy")
        binding.etStartDateEvent.transformIntoDatePicker("dd MMM yyyy")
        binding.etEndDateEvent.transformIntoDatePicker("dd MMM yyyy")
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