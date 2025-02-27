package com.just_for_fun.justforfun.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PosterItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
    }
}