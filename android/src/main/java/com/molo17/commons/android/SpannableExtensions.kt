package com.molo17.commons.android

import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.set

/**
 * Created by Damiano Giusti on 2019-06-03.
 */

inline fun spannable(string: String = "", block: SpannableStringBuilder.() -> Unit): SpannableStringBuilder =
    SpannableStringBuilder(string).apply(block)

inline fun SpannableStringBuilder.clickable(text: String, crossinline onClick: (View) -> Unit) {
    val (start, end) = indexOf(text).let { it to it + text.count() }
    set(start, end, object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick(widget)
        }
    })
}