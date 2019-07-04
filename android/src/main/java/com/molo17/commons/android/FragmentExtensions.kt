package com.molo17.commons.android

import android.view.ViewGroup
import androidx.appcompat.widget.ActionMenuView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.jetbrains.anko.childrenRecursiveSequence

/**
 * Created by Damiano Giusti on 2019-06-04.
 */

inline fun <reified VM : ViewModel> Fragment.getFragmentViewModel(): VM =
    ViewModelProviders.of(this, (requireActivity() as HasViewModelFactory).viewModelFactory).get(VM::class.java)

inline fun <reified VM : ViewModel> Fragment.getActivityViewModel(): VM =
    requireActivity().run { ViewModelProviders.of(this, (this as HasViewModelFactory).viewModelFactory).get(VM::class.java) }

inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(): VM =
    ViewModelProviders.of(this, (this as HasViewModelFactory).viewModelFactory).get(VM::class.java)

val Fragment.actionMenuView: ActionMenuView?
    get() {
        val view = activity?.window?.decorView as? ViewGroup ?: return null
        return view.childrenRecursiveSequence().find { it is ActionMenuView } as? ActionMenuView
    }

fun Fragment.observeErrorsOf(errorHandler: UiErrorHandler) {
    errorHandler.errorMessageResIds().observe(this, Observer {
        it.popValue()?.also { resId ->
            val msg = getString(resId)
            activity?.showErrorAlert(msg)
        }
    })
}

interface HasViewModelFactory {

    var viewModelFactory: ViewModelProvider.Factory
}