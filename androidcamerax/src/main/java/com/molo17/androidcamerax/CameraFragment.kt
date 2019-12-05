package com.molo17.androidcamerax

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.molo17.androidcamerax.api21.Api21CameraFragment
import com.molo17.androidcamerax.internal.Event
import com.molo17.androidcamerax.legacy.LegacyCameraFragment
import java.io.File

/**
 * Created by Damiano Giusti on 2019-05-23.
 */
abstract class CameraFragment : Fragment() {

    enum class FlashMode { ON, OFF }
    enum class CameraMode { FRONT, BACK }

    protected val cameraErrors = MutableLiveData<Throwable>()
    private val hasCameraPermissions = MutableLiveData<Boolean>().apply { value = false }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val context = requireContext()
        if (hasCameraPermissions(context) && hasStoragePermissions(context)) {
            onPermissionsGranted()
            onReady()
        } else {
            hasCameraPermissions.value = false
        }
    }

    abstract fun takePicture(outputFile: File): LiveData<Event<PictureResult>>

    abstract fun enableFlash()

    abstract fun disableFlash()

    abstract fun switchToFrontCamera()

    abstract fun switchToBackCamera()

    fun cameraErrors(): LiveData<Throwable> = cameraErrors

    fun cameraPermissions(): LiveData<Boolean> = hasCameraPermissions

    internal abstract fun onReady()

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    private fun onPermissionsGranted() {
        hasCameraPermissions.value = true
    }

    fun checkPermissions() {
        val activity = requireActivity()
        if (hasCameraPermissions(activity) && hasStoragePermissions(activity)) {
            onPermissionsGranted()
            onReady()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    private fun hasCameraPermissions(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun hasStoragePermissions(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    protected val cameraModeArg: CameraMode get() =
        arguments?.getInt(KEY_CAMERA)?.let(CameraMode.values()::get) ?: error()

    protected val flashModeArg: FlashMode get() =
        arguments?.getInt(KEY_FLASH)?.let(FlashMode.values()::get) ?: error()

    companion object {
        const val TAG = "CameraFragment"

        private const val KEY_FLASH = "flashOn"
        private const val KEY_CAMERA = "whichCamera"

        @JvmStatic fun newInstance(flash: FlashMode, camera: CameraMode): CameraFragment {
            val fragment =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) Api21CameraFragment()
                else LegacyCameraFragment()

            fragment.arguments = Bundle().apply {
                putInt(KEY_FLASH, flash.ordinal)
                putInt(KEY_CAMERA, camera.ordinal)
            }
            return fragment
        }

        private fun error(): Nothing =
            error("Create a CameraFragment instance using the CameraFragment#newInstance(...) method!")
    }
}

