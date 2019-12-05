package com.molo17.androidcamerax

import java.io.File

/**
 * Created by Damiano Giusti on 2019-05-24.
 */
sealed class PictureResult {
    data class Success(val file: File) : PictureResult()
    data class Failure(val error: PictureException) : PictureResult()
}

sealed class PictureException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)

class FileSaveException(cause: Throwable? = null) :
    PictureException("Error saving picture in specified file", cause)

class ExifDataException(cause: Throwable? = null) :
    PictureException("Error getting EXIF interface data from picture file", cause)

class JpegDecodeException(cause: Throwable? = null) :
    PictureException("Error decoding a Bitmap from picture file", cause)

class PictureUnhandledException(cause: Throwable? = null) :
    PictureException("An unexpected error occurred while saving file", cause)