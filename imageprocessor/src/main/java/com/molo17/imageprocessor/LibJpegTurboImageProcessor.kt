package com.molo17.imageprocessor

import android.graphics.Rect
import jpegkit.JpegFile
import java.io.File
import javax.inject.Inject

class LibJpegTurboImageProcessor @Inject constructor() : CommonImageProcessor() {
    override fun getImageSize(path: String): ImageProcessor.Size {
        val jpeg = jpegFile(path)
        return try {
            ImageProcessor.Size(jpeg.width, jpeg.height)
        } finally {
            jpeg.release()
        }
    }

    override fun cutImage(path: String, rect: Rect) {
        val jpeg = jpegFile(path)
        try {
            jpeg.crop(rect)
            jpeg.save()
        } finally {
            jpeg.release()
        }
    }

    override fun rotateImage(path: String, degrees: Int) {
        val jpeg = jpegFile(path)
        try {
            jpeg.rotate(degrees)
            jpeg.save()
            trySetExifRotation(path, -degrees)
        } finally {
            jpeg.release()
        }
    }

    private fun jpegFile(path: String) = JpegFile(File(path))
}