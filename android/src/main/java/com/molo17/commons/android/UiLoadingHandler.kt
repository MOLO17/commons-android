package com.molo17.commons.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by Damiano Giusti on 2019-06-19.
 */
interface UiLoadingHandler {

    fun isLoading(): LiveData<Boolean>

    fun <T> T.startLoading() where T : UiLoadingHandler {
        mutableLoading()?.postValue(true)
    }

    fun <T> T.stopLoading() where T : UiLoadingHandler {
        mutableLoading()?.postValue(false)
    }
}

private fun UiLoadingHandler.mutableLoading() = isLoading() as? MutableLiveData<Boolean>

@Suppress("FunctionName")
fun UiLoadingHandler(startValue: Boolean) = object : UiLoadingHandler {

    private val loading = MutableLiveData<Boolean>().apply { value = startValue }

    override fun isLoading(): LiveData<Boolean> = loading
}