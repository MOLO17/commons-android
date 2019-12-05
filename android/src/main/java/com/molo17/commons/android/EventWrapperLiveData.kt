package com.molo17.commons.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Damiano Giusti on 2019-05-31.
 */
data class Event<T>(private val base: T) {
    private var consumed: Boolean = false

    fun popValue(): T? =
        if (consumed) null
        else base.also { consumed = true }

    fun peekValue(): T = base
}

class EventLiveData<T> : MutableLiveData<Event<T>>()

fun <T> MutableLiveData<Event<T>>.postEvent(value: T) = postValue(Event(value))

fun <T> LiveData<Event<T>>.unwrapEvent(): LiveData<T> = object : LiveData<T>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        this@unwrapEvent.observe(owner, Observer { event ->
            event?.popValue()?.also(observer::onChanged)
        })
    }

    override fun observeForever(observer: Observer<in T>) {
        this@unwrapEvent.observeForever { event ->
            event?.popValue()?.also(observer::onChanged)
        }
    }
}