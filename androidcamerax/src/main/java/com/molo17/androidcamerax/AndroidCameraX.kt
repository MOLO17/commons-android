package com.molo17.androidcamerax

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.molo17.androidcamerax.api21.Api21CameraViewModel
import com.molo17.androidcamerax.legacy.LegacyCameraViewModel
import com.molo17.imageprocessor.LibJpegTurboImageProcessor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
object AndroidCameraX {

    private var workExecutor: Executor = Executors.newSingleThreadExecutor()

    fun init(executor: Executor) {
        this.workExecutor = executor
    }

    internal inline fun <reified T> getViewModel(fragment: CameraFragment): T where T : CameraViewModel, T : ViewModel =
        ViewModelProviders.of(fragment, viewModelFactory).get(T::class.java)

    private val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                Api21CameraViewModel::class.java -> Api21CameraViewModel(workExecutor, LibJpegTurboImageProcessor()) as T
                LegacyCameraViewModel::class.java -> LegacyCameraViewModel(workExecutor, LibJpegTurboImageProcessor()) as T
                else -> throw IllegalArgumentException("${modelClass.simpleName} is not handled by AndroidCameraX")
            }
        }
    }
}