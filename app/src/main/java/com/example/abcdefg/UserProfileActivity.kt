package com.example.abcdefg

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.abcdefg.databinding.ActivityGroupMainBinding
import com.example.abcdefg.databinding.ActivityUserProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

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
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
        }

        auth = Firebase.auth

        // bind view pager adapter
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vp2User.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tlBlogs, binding.vp2User) { tab, position ->
            tab.text = BlogTab[position]
        }.attach()

        populateData()

        binding.cvUserCard.setOnClickListener {
            EditUserProfileFragment().show(supportFragmentManager, "editProfile")
        }

        binding.ibClose.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully, please log in", Toast.LENGTH_SHORT).show()
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finishAffinity()
        }
    }

    private fun populateData() {
        val db = Firebase.firestore

        db.collection("users").whereEqualTo("uid", auth.uid).addSnapshotListener { value, err ->
            if (err != null) {
                Log.d("EditUserProfile", "Cannot listen to snapshot changes")
                return@addSnapshotListener
            }
            binding.tvDisplayName.text = value!!.documents[0]?.get("name")!!.toString()
            if (value!!.documents[0]?.get("imagePath") != null && value!!.documents[0]?.get("imagePath")!!.toString().isNotBlank()) {
                Picasso.get().load(value!!.documents[0]?.get("imagePath")!!.toString()).into(binding.sivPfp)
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
}