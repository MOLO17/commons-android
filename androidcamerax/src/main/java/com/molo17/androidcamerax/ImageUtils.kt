package com.molo17.androidcamerax

import com.molo17.imageprocessor.ImageProcessor
import java.io.File
import java.io.IOException

internal fun adjustImageOrientation(path: String, imageProcessor: ImageProcessor): PictureResult {
    val degrees = try {
        imageProcessor.getExifRotation(path)
    } catch (io: IOException) {
        return PictureResult.Failure(ExifDataException())
    }
    return try {
        imageProcessor.rotateImage(path, degrees)
        PictureResult.Success(File(path))
    } catch (e: IOException) {
        PictureResult.Failure(FileSaveException())
    }
}