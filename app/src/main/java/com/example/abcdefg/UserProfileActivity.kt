package com.example.abcdefg

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.databinding.ActivityUserProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

val BlogTab = arrayOf(
    "My Blogs",
    "Saved Blogs"
)
class ViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount(): Int {
        return 2 // always got two tabs only
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyBlogsFragment()
            1 -> SavedBlogsFragment()
            else -> MyBlogsFragment() // wont trigger but needed
        }
    }

}

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
        }

        // bind view pager adapter
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vp2User.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tlBlogs, binding.vp2User) { tab, position ->
            tab.text = BlogTab[position]
        }.attach()
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