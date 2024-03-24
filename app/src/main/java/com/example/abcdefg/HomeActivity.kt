package com.example.abcdefg

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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

        // bind bottom nav bar to nav controller
        val btmNavHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        binding.btmNav.setupWithNavController(btmNavHostFrag.navController)
        binding.btmNav.setOnItemSelectedListener {
            if (it.itemId == R.id.userProfile) {
                startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
                return@setOnItemSelectedListener false
            }
            btmNavHostFrag.navController.navigate(it.itemId)
            true
        }

        // expand the navigation bottom bar
        binding.btnExpandNav.setOnClickListener {
            // hide keyboard first
            Utils.hideSoftKeyboard(this, it)
            binding.btmNav.visibility = if (binding.btmNav.visibility == View.GONE) {
                binding.btnExpandNav.animate().rotation(180f).setDuration(500).start()
                View.VISIBLE
            } else {
                binding.btnExpandNav.animate().rotation(0f).setDuration(500).start()
                View.GONE
            }
        }
    }
}