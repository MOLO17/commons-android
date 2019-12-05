package com.molo17.imageprocessor

import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import kotlin.math.abs

abstract class CommonImageProcessor : ImageProcessor {
    override fun getExifRotation(path: String): Int? {
        val exif = ExifInterface(path)
        return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_NORMAL -> 0
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> null
        }
    }

    override fun getScaleFactors(matrix: Matrix): Pair<Float, Float> {
        // Get image matrix values and place them in an array.
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        val scaleX = matrixValues[Matrix.MSCALE_X]
        val scaleY = matrixValues[Matrix.MSCALE_Y]

        return scaleX to scaleY
    }

    override fun getTranslationFactors(matrix: Matrix): Pair<Float, Float> {
        // Get image matrix values and place them in an array.
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]

        return transX to transY
    }

    protected fun trySetExifRotation(path: String, degrees: Int) {
        try {
            ExifInterface(path).apply {
                rotate(degrees)
                saveAttributes()
            }
        } catch (e: IOException) {
            // Thrown when the image is not a JPEG image.
        }
    }
}
