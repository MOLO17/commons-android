package com.molo17.androidcamerax.api21

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.camera2.Camera2AppConfig
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LiveData
import com.molo17.androidcamerax.AndroidCameraX
import com.molo17.androidcamerax.CameraFragment
import com.molo17.androidcamerax.PictureResult
import com.molo17.androidcamerax.R
import com.molo17.androidcamerax.internal.AutoFitPreviewBuilder
import com.molo17.androidcamerax.internal.Event
import java.io.File

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class Api21CameraFragment : CameraFragment() {

    private lateinit var textureView: TextureView

    private lateinit var viewModel: Api21CameraViewModel

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = AndroidCameraX.getViewModel(this)
        viewModel.setup(cameraModeArg, flashModeArg)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camerax, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textureView = view.findViewById(R.id.texture_view)
        checkPermissions()
    }

    override fun onDestroyView() {
        CameraX.unbindAll()
        super.onDestroyView()
    }

    override fun onReady() {
        textureView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> updateTransform() }

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                textureView.post { startPreview() }
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public methods
    ///////////////////////////////////////////////////////////////////////////

    override fun takePicture(outputFile: File): LiveData<Event<PictureResult>> {
        imageCapture?.also { CameraX.unbind(it) }
        val imageCapture = createImageCapture().also { imageCapture = it }
        imageCapture.takePicture(outputFile, object : ImageCapture.OnImageSavedListener {
            override fun onImageSaved(file: File) {
                viewModel.handleTakePictureResult(file)
            }

            override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                viewModel.handleTakePictureResult(useCaseError, cause)
            }
        })
        return viewModel.takenPictureState()
    }

    override fun enableFlash() {
        viewModel.isFlashEnabled = true
    }

    override fun disableFlash() {
        viewModel.isFlashEnabled = false
    }

    override fun switchToFrontCamera() {
        viewModel.cameraMode = CameraMode.FRONT
        view?.post { startPreview() }
    }

    override fun switchToBackCamera() {
        viewModel.cameraMode = CameraMode.BACK
        view?.post { startPreview() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    private fun startPreview() {
        CameraX.unbindAll()
        val previewConfig = PreviewConfig.Builder()
            .setLensFacing(viewModel.lensFacing())
            .build()

        val preview = AutoFitPreviewBuilder.build(previewConfig, textureView)
        CameraX.bindToLifecycle(this, preview)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = textureView.width / 2f
        val centerY = textureView.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (textureView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix)
    }

    private fun createImageCapture() = ImageCaptureConfig.Builder()
        .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        .setFlashMode(viewModel.flashMode())
        .setLensFacing(viewModel.lensFacing())
        .setTargetRotation(textureView.display.rotation)
        .build()
        .let { ImageCapture(it) }
        .also { CameraX.bindToLifecycle(this, it) }
}

internal class Camera2Initializer : ContentProvider() {
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(): Boolean {
        try {
            CameraX.init(context, Camera2AppConfig.create(context))
        } catch (ignored: NoClassDefFoundError) {
        }
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}