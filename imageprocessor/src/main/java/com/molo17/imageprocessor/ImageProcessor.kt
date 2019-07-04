package com.molo17.imageprocessor

import android.graphics.Matrix
import android.graphics.Rect

/**
 * Created by Damiano Giusti on 2019-06-21.
 */
interface ImageProcessor {

    fun getExifRotation(path: String): Int

    fun getImageSize(path: String): Size

    fun getScaleFactors(matrix: Matrix): Pair<Float, Float>

    fun getTranslationFactors(matrix: Matrix): Pair<Float, Float>

    fun rotateImage(path: String, degrees: Int)

    fun cutImage(path: String, rect: Rect)

    data class Size(val width: Int, val height: Int)
}

