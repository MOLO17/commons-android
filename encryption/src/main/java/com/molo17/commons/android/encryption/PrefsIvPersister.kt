package com.molo17.commons.android.encryption

import android.content.Context
import com.molo17.commons.android.encryption.symmetric.BaseSymmetricEncryptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Damiano Giusti on 2019-06-12.
 */

private const val IV_PREF_NAME = "iv"
private const val IV_KEY = "iv"

@Singleton
class PrefsIvPersister @Inject constructor(
    private val context: Context,
    private val base64Converter: Base64Converter
): BaseSymmetricEncryptor.IvPersister {

    private val prefs by lazy { context.getSharedPreferences(IV_PREF_NAME, Context.MODE_PRIVATE) }

    override fun saveInitializationVector(iv: ByteArray) {
        val value = base64Converter.encodeToString(iv)
        prefs.edit().putString(IV_KEY, value).apply()
    }

    override fun getInitializationVector(): ByteArray? {
        return prefs.getString(IV_KEY, null)?.let(base64Converter::decode)
    }
}