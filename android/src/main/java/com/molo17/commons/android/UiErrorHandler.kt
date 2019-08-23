package com.molo17.commons.android

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

/**
 * Created by Damiano Giusti on 2019-06-03.
 */
interface UiErrorHandler {

    fun errorMessageResIds(): LiveData<Event<Int>>

    fun handleError(error: Throwable)
}

fun UiErrorHandler.onError(error: Throwable) {
    UiErrorHandlerPlugins.logError(error)
    handleError(error)
}

object UiErrorHandlerPlugins {
    internal var onError: (Fragment, String) -> Unit = { fragment, msg -> fragment.activity?.showErrorAlert(msg) }
    internal var logError: (Throwable) -> Unit = {}

    fun setOnErrorHandler(hook: (Fragment, String) -> Unit) {
        onError = hook
    }

    fun setErrorLogger(hook: (Throwable) -> Unit) {
        logError = hook
    }
}