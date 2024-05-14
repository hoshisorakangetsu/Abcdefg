package com.example.abcdefg

import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.abcdefg.databinding.ActivityHomeBinding
import com.example.abcdefg.utils.Utils

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
        }

        fun toggleBottomNavVisibility() {
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                binding.btnExpandNav.animate().rotation(180f).setDuration(500).start()
                View.VISIBLE
            } else {
                binding.btnExpandNav.animate().rotation(0f).setDuration(500).start()
                View.GONE
            }

        }

        // bind bottom nav bar to nav controller
        val btmNavHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        binding.btmNav.setupWithNavController(btmNavHostFrag.navController)
        binding.btmNav.setOnItemSelectedListener {
            toggleBottomNavVisibility()
            if (it.itemId == R.id.userProfile) {
                startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
                return@setOnItemSelectedListener false
            }
            // hide all controls first (except the first one which is the expand nav button
            for (i in 1..< binding.llActions.childCount) {
                binding.llActions.getChildAt(i)?.visibility = View.GONE
            }

            if (it.itemId == R.id.postListFragment) {
                binding.blogsControl.visibility = View.VISIBLE
            } else if (it.itemId == R.id.joinedGroupListFragment) {
                binding.groupsControl.visibility = View.VISIBLE
            }
            btmNavHostFrag.navController.navigate(it.itemId)
            true
        }

        // expand the navigation bottom bar
        binding.btnExpandNav.setOnClickListener {
            // hide keyboard first
            Utils.hideSoftKeyboard(this, it)
            toggleBottomNavVisibility()
        }

        // find groups button clicked
        binding.btnFindGroups.setOnClickListener {
            val exploreGroupIntent = Intent(this@HomeActivity, GroupExploreActivity::class.java)
            startActivity(exploreGroupIntent)
        }

        binding.btnNewGroup.setOnClickListener {
            startActivity(Intent(this, GroupCreateActivity::class.java))
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
}