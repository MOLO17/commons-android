package com.molo17.androidcamerax.legacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.cameraview.CameraView
import com.molo17.androidcamerax.CameraFragment
import com.molo17.androidcamerax.CameraViewModel
import com.molo17.androidcamerax.FileSaveException
import com.molo17.androidcamerax.PictureResult
import com.molo17.androidcamerax.adjustImageOrientation
import com.molo17.androidcamerax.internal.Event
import com.molo17.androidcamerax.internal.postEvent
import com.molo17.imageprocessor.ImageProcessor
import java.io.File
import java.io.IOException
import java.util.concurrent.Executor

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
internal class LegacyCameraViewModel(
    val executor: Executor,
    private val imageProcessor: ImageProcessor
): ViewModel(), CameraViewModel {

    lateinit var flashMode: CameraFragment.FlashMode
    lateinit var cameraMode: CameraFragment.CameraMode

    private val takePictureResult = MutableLiveData<Event<PictureResult>>()

    override fun takenPictureState(): LiveData<Event<PictureResult>> = takePictureResult

    fun handlePictureData(outputFile: File, data: ByteArray) {
        executor.execute {
            try {
                outputFile.writeBytes(data)
                takePictureResult.postEvent(adjustImageOrientation(outputFile.absolutePath, imageProcessor))
            } catch (error: IOException) {
                takePictureResult.postEvent(PictureResult.Failure(FileSaveException()))
            }
        }
    }
}

fun CameraFragment.CameraMode.toCameraView() =
    if (this == CameraFragment.CameraMode.FRONT) CameraView.FACING_FRONT else CameraView.FACING_BACK

fun CameraFragment.FlashMode.toCameraView() =
    if (this == CameraFragment.FlashMode.ON) CameraView.FLASH_ON else CameraView.FLASH_OFF