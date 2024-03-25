package com.example.abcdefg

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_from_bottom, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.slide_out_from_top)
        } else {
            overridePendingTransition(R.anim.slide_in_from_bottom, 0)
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