package com.molo17.commons.android

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Matteo Sist on 2019-07-04.
 */

fun RecyclerView.findVisibleItemViewHolder(): RecyclerView.ViewHolder? {
    return layoutManager?.findViewByPosition(findFirstCompletelyVisibleItemPosition())?.let(this::getChildViewHolder)
}

fun RecyclerView.onScrolled(callback: (position: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val position = findFirstVisibleItemPosition()
            if (newState == RecyclerView.SCROLL_STATE_IDLE && position != RecyclerView.NO_POSITION) {
                callback(position)
            }
        }
    })
}

fun RecyclerView.findFirstCompletelyVisibleItemPosition() = when (val manager = layoutManager) {
    is LinearLayoutManager -> manager.findFirstCompletelyVisibleItemPosition()
    is GridLayoutManager -> manager.findFirstCompletelyVisibleItemPosition()
    null -> error("RecyclerView has no LayoutManager attached")
    else -> error("${manager::class.java} is not supported")
}

fun RecyclerView.findFirstVisibleItemPosition() = when (val manager = layoutManager) {
    is LinearLayoutManager -> manager.findFirstVisibleItemPosition()
    is GridLayoutManager -> manager.findFirstVisibleItemPosition()
    null -> error("RecyclerView has no LayoutManager attached")
    else -> error("${manager::class.java} is not supported")
}