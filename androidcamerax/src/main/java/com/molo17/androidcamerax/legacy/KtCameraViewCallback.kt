package com.molo17.androidcamerax.legacy

import com.google.android.cameraview.CameraView

internal class KtCameraViewCallback(
    private val onCameraOpened: ((CameraView) -> Unit)? = null,
    private val onCameraClosed: ((CameraView) -> Unit)? = null,
    private val onPictureTaken: ((CameraView, ByteArray) -> Unit)? = null
) : CameraView.Callback() {
    override fun onCameraOpened(cameraView: CameraView) {
        onCameraOpened?.invoke(cameraView)
    }

    override fun onCameraClosed(cameraView: CameraView) {
        onCameraClosed?.invoke(cameraView)
    }

    override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
        onPictureTaken?.invoke(cameraView, data)
    }
}