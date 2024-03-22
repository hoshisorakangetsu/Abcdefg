package com.example.abcdefg

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.utils.Utils
import com.example.abcdefg.viewmodels.GroupViewModel

class GroupMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupMainBinding

    private val groupViewModel: GroupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup app bar
        setSupportActionBar(binding.topAppbar)
        // TODO make this title dynamic
        supportActionBar?.title = "Study Group 1"

        // bind bottom nav bar to nav controller
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.btmNav.setupWithNavController(navHostFrag.navController)

        // FIXME not working if navigate to Topic -> Topic Content -> Chat / Event -> Topic
        binding.btmNav.setOnItemReselectedListener { item ->
            // Pop everything up to the reselected item
            val reselectedDestinationId = item.itemId
            navHostFrag.navController.popBackStack(reselectedDestinationId, inclusive = false)
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

        binding.etChatMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.etChatMessage.text.isNotBlank()) {
                binding.btnSendMessage.visibility = View.VISIBLE
            } else {
                binding.btnSendMessage.visibility = View.GONE
            }
        }
    }
}