package com.molo17.androidcamerax.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.google.android.cameraview.CameraView
import com.molo17.androidcamerax.AndroidCameraX
import com.molo17.androidcamerax.CameraFragment
import com.molo17.androidcamerax.PictureResult
import com.molo17.androidcamerax.R
import com.molo17.androidcamerax.internal.Event
import java.io.File

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
internal class LegacyCameraFragment : CameraFragment() {

    private lateinit var cameraView: CameraView

    private lateinit var viewModel: LegacyCameraViewModel
    private var cameraCallback: CameraView.Callback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_legacy_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraView = view.findViewById(R.id.camera_view)
        cameraView.flash = flashModeArg.toCameraView()
        cameraView.facing = cameraModeArg.toCameraView()

        viewModel = AndroidCameraX.getViewModel(this)
        viewModel.flashMode = flashModeArg
        viewModel.cameraMode = cameraModeArg

        checkPermissions()
    }

    override fun onDestroyView() {
        cameraCallback?.let(cameraView::removeCallback)
        super.onDestroyView()
    }

    override fun takePicture(outputFile: File): LiveData<Event<PictureResult>> {
        cameraCallback = KtCameraViewCallback(onPictureTaken = { _, data ->
            cameraCallback?.let(cameraView::removeCallback)
            viewModel.handlePictureData(outputFile, data)
        }).also(cameraView::addCallback)
        cameraView.takePicture()
        return viewModel.takenPictureState()
    }

    override fun enableFlash() {
        viewModel.flashMode = FlashMode.ON
        cameraView.flash = CameraView.FLASH_ON
    }

    override fun disableFlash() {
        viewModel.flashMode = FlashMode.OFF
        cameraView.flash = CameraView.FLASH_OFF
    }

    override fun switchToFrontCamera() {
        viewModel.cameraMode = CameraMode.FRONT
        cameraView.facing = CameraView.FACING_FRONT
        cameraView.flash = viewModel.flashMode.toCameraView()
    }

    override fun switchToBackCamera() {
        viewModel.cameraMode = CameraMode.BACK
        cameraView.facing = CameraView.FACING_BACK
        cameraView.flash = viewModel.flashMode.toCameraView()
    }

    override fun onReady() {
        // ignored.
    }

    override fun onStart() {
        super.onStart()
        viewModel.executor.execute(this::startCameraView)
    }

    override fun onStop() {
        super.onStop()
        viewModel.executor.execute(this::stopCameraView)
    }

    private fun startCameraView() {
        try {
            cameraView.start()
        } catch (e: Exception) {
            cameraErrors.postValue(e)
        }
    }

    private fun stopCameraView() {
        try {
            cameraView.stop()
        } catch (e: Exception) {
            cameraErrors.postValue(e)
        }
    }
}

