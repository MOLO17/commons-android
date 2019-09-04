package com.molo17.commons.android

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Damiano Giusti on 2019-06-17.
 */

/**
 * A simple abstraction over [ViewModel] that is used for delivering results between fragments.
 *
 * @see CameraResultDispatcher
 */
abstract class ResultDispatcher : ViewModel()

/**
 * Convenience class for obtaining an instance of a [ResultDispatcher] of the given class [type].
 */
abstract class ResultDispatcherFactory<T>(private val type: Class<out T>) where T : ResultDispatcher {
    fun get(fragment: Fragment): T = ViewModelProviders.of(fragment.requireActivity()).get(type)
}