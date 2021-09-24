package com.molo17.commons.android

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ActionMenuView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

inline fun <reified VM : ViewModel> Fragment.getFragmentViewModel(): VM =
    ViewModelProvider(this, (requireActivity() as HasViewModelFactory).viewModelFactory).get(VM::class.java)

inline fun <reified VM : ViewModel> Fragment.getActivityViewModel(): VM =
    requireActivity().run { ViewModelProvider(this, (this as HasViewModelFactory).viewModelFactory).get(VM::class.java) }

inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(): VM =
    ViewModelProvider(this, (this as HasViewModelFactory).viewModelFactory).get(VM::class.java)

val Fragment.actionMenuView: ActionMenuView?
    get() {
        val view = activity?.window?.decorView as? ViewGroup ?: return null
        return view.childrenRecursiveSequence().find { it is ActionMenuView } as? ActionMenuView
    }

fun Fragment.observeErrorsOf(errorHandler: UiErrorHandler) {
    errorHandler.errorMessageResIds().observe(this, {
        it.popValue()?.also { resId ->
            val msg = getString(resId)
            UiErrorHandlerPlugins.onError(this, msg)
        }
    })
}

interface HasViewModelFactory {
    var viewModelFactory: ViewModelProvider.Factory
}

/**
 * Return the [Sequence] of all children of the received [View], recursively.
 * Note that the sequence is not thread-safe.
 *
 * @return the [Sequence] of children.
 */
fun View.childrenRecursiveSequence(): Sequence<View> = ViewChildrenRecursiveSequence(this)

private class ViewChildrenSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return ViewIterator(view)
    }

    private class ViewIterator(private val view: ViewGroup) : Iterator<View> {
        private var index = 0
        private val count = view.childCount

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            return view.getChildAt(index++)
        }

        override fun hasNext(): Boolean {
            checkCount()
            return index < count
        }

        private fun checkCount() {
            if (count != view.childCount) throw ConcurrentModificationException()
        }
    }
}

private class ViewChildrenRecursiveSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return RecursiveViewIterator(view)
    }

    private class RecursiveViewIterator(view: View) : Iterator<View> {
        private val sequences = arrayListOf(ViewChildrenSequence(view))
        private var current = sequences.removeLast().iterator()

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            val view = current.next()
            if (view is ViewGroup && view.childCount > 0) {
                sequences.add(ViewChildrenSequence(view))
            }
            return view
        }

        override fun hasNext(): Boolean {
            if (!current.hasNext() && sequences.isNotEmpty()) {
                current = sequences.removeLast().iterator()
            }
            return current.hasNext()
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun <T : Any> MutableList<T>.removeLast(): T {
            if (isEmpty()) throw NoSuchElementException()
            return removeAt(size - 1)
        }
    }
}