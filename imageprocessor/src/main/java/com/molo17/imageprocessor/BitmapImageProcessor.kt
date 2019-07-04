package com.molo17.imageprocessor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import javax.inject.Inject

class BitmapImageProcessor @Inject constructor() : CommonImageProcessor() {
    override fun getImageSize(path: String): ImageProcessor.Size {
        val bitmap = BitmapFactory.decodeFile(path)
        return ImageProcessor.Size(bitmap.width, bitmap.height).also { bitmap.recycle() }
    }

    override fun cutImage(path: String, rect: Rect) {
        val source = BitmapFactory.decodeFile(path)
        val transformed = Bitmap.createBitmap(source, rect.left, rect.top, rect.width(), rect.height())
        transformed.saveTo(path)
        if (!transformed.isRecycled) transformed.recycle()
        if (!source.isRecycled) source.recycle()
    }

    override fun rotateImage(path: String, degrees: Int) {
        val source = BitmapFactory.decodeFile(path)
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        val transformed = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
        transformed.saveTo(path)
        if (!transformed.isRecycled) transformed.recycle()
        if (!source.isRecycled) source.recycle()
        trySetExifRotation(path, degrees)
    }
}