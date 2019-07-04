package com.molo17.androidcamerax.internal

import androidx.lifecycle.MutableLiveData

data class Event<T>(private val base: T) {
    private var consumed: Boolean = false

    fun popValue(): T? =
        if (consumed) null
        else base.also { consumed = true }

    fun peekValue(): T = base
}

fun <T> MutableLiveData<Event<T>>.postEvent(value: T) = postValue(Event(value))