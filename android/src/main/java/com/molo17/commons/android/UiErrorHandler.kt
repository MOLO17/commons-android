package com.molo17.commons.android

import androidx.lifecycle.LiveData

/**
 * Created by Damiano Giusti on 2019-06-03.
 */
interface UiErrorHandler {

    fun errorMessageResIds(): LiveData<Event<Int>>

    fun handleError(error: Throwable)
}

fun UiErrorHandler.onError(error: Throwable) {
    handleError(error)
}
