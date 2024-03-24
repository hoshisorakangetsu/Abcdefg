package com.example.abcdefg.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            val displayDensity = view.resources.displayMetrics.density
            val spacing = (spaceSize * displayDensity + 0.5f).toInt()

            if (parent.getChildAdapterPosition(view) != 0) {
                top = spacing
            }
            left = spaceSize
            right = spaceSize
            bottom = spaceSize
        }
    }
}