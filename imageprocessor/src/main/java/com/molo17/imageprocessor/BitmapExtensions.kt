package com.molo17.imageprocessor

import android.graphics.Bitmap
import java.io.File

/**
 * Created by Damiano Giusti on 2019-06-24.
 */

fun Bitmap.saveTo(path: String) {
    File(path).outputStream().use { os -> compress(Bitmap.CompressFormat.JPEG, 100, os) }
}