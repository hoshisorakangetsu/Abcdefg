package com.example.abcdefg.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.abcdefg.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable


class Utils {

    public companion object {
        fun hideSoftKeyboard(activity: Activity, view: View) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0)
        }

        fun createChip(context: Context, tag: String): Chip {
            val chip = Chip(context)
            chip.text = tag
            val drawable =
                ChipDrawable.createFromAttributes(context, null, 0, R.style.PrimaryChip)
            chip.setChipDrawable(drawable)
            chip.isChipIconVisible = false
            chip.isCloseIconVisible = false
            chip.isCheckable = false
            chip.chipStrokeWidth = 0f
            chip.minHeight = 0
            chip.minWidth = 0
            chip.chipStrokeColor = null
            return chip
        }
    }
}