package com.molo17.commons.android

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] decorator which adds a spacing to the left of each item of the RecyclerView.
 *
 * @param leftSpacing The dimension resource used for getting the spacing.
 */
class HorizontalSpacingDecoration(@DimenRes private val leftSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = view.resources.getDimensionPixelOffset(leftSpacing)
    }
}

/**
 * [RecyclerView] decorator which adds a spacing at the bottom of each item of the RecyclerView.
 *
 * @param bottomSpacing The dimension resource used for getting the spacing.
 */
class VerticalSpacingDecoration(@DimenRes private val bottomSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = view.resources.getDimensionPixelOffset(bottomSpacing)
    }
}