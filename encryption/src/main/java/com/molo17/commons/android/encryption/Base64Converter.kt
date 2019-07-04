package com.molo17.commons.android.encryption

import android.annotation.TargetApi
import android.os.Build
import java.util.*

/**
 * Created by Damiano Giusti on 2019-05-13.
 */
interface Base64Converter {
    fun encodeToString(value: ByteArray): String
    fun decode(value: String): ByteArray
}

class AndroidBase64Converter : Base64Converter {
    override fun encodeToString(value: ByteArray): String =
        android.util.Base64.encodeToString(value, android.util.Base64.NO_WRAP)

    override fun decode(value: String): ByteArray =
        android.util.Base64.decode(value, android.util.Base64.NO_WRAP)

}

@TargetApi(Build.VERSION_CODES.O)
class JavaBase64Converter : Base64Converter {
    override fun encodeToString(value: ByteArray): String =
        Base64.getEncoder().encodeToString(value)

    override fun decode(value: String): ByteArray =
        Base64.getDecoder().decode(value)
}

@Suppress("FunctionName") fun Base64Converter(): Base64Converter =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) JavaBase64Converter()
    else AndroidBase64Converter()