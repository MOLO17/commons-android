package com.molo17.androidcamerax.api21

import androidx.camera.core.CameraX
import androidx.camera.core.FlashMode
import androidx.camera.core.ImageCapture
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.molo17.androidcamerax.*
import com.molo17.androidcamerax.internal.Event
import com.molo17.androidcamerax.internal.postEvent
import com.molo17.imageprocessor.ImageProcessor
import java.io.File
import java.util.concurrent.Executor

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
internal class Api21CameraViewModel(
    private val executor: Executor,
    private val imageProcessor: ImageProcessor
) : ViewModel(), CameraViewModel {

    private val pictureResult = MutableLiveData<Event<PictureResult>>()

    var isFlashEnabled: Boolean
        get() = flashEnabled ?: error("Call #setup(...) first!")
        set(value) { flashEnabled = value }

    var cameraMode: CameraFragment.CameraMode
        get() = currentCameraMode ?: error("Call #setup(...) first!")
        set(value) { currentCameraMode = value }

    private var flashEnabled: Boolean? = null
    private var currentCameraMode: CameraFragment.CameraMode? = null

    fun flashMode(): FlashMode =
        if (isFlashEnabled) FlashMode.ON
        else FlashMode.OFF

    fun lensFacing(): CameraX.LensFacing =
        if (cameraMode == CameraFragment.CameraMode.FRONT) CameraX.LensFacing.FRONT
        else CameraX.LensFacing.BACK

    override fun takenPictureState(): LiveData<Event<PictureResult>> = pictureResult

    fun handleTakePictureResult(file: File) {
        executor.execute {
            pictureResult.postEvent(adjustImageOrientation(file.absolutePath, imageProcessor))
        }
    }

    fun handleTakePictureResult(useCaseError: ImageCapture.UseCaseError, cause: Throwable?) {
        val error = when (useCaseError) {
            ImageCapture.UseCaseError.UNKNOWN_ERROR -> PictureUnhandledException(cause)
            ImageCapture.UseCaseError.FILE_IO_ERROR -> FileSaveException(cause)
        }
        pictureResult.postEvent(PictureResult.Failure(error))
    }

    fun setup(cameraMode: CameraFragment.CameraMode, flashMode: CameraFragment.FlashMode) {
        if (currentCameraMode == null || flashEnabled == null) {
            currentCameraMode = cameraMode
            flashEnabled = (flashMode == CameraFragment.FlashMode.ON)
        }
    }
}