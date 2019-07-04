package com.molo17.commons.android

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.ActionMenuView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

val ActionMenuView.overflowButton: ImageView?
    get() = children.find { it is ImageView } as ImageView?

fun ActionMenuView.setOverflowTint(@ColorInt color: Int) {
    val view = overflowButton ?: return
    val drawable = view.drawable.mutate()
    DrawableCompat.setTint(drawable, color)
    view.setImageDrawable(drawable)
}