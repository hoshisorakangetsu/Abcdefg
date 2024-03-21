package com.example.abcdefg.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class Utils {

    public companion object {
        fun hideSoftKeyboard(activity: Activity, view: View) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0)
        }
    }
}