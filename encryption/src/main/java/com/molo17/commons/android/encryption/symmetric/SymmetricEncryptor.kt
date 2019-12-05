package com.molo17.commons.android.encryption.symmetric

import android.os.Build
import javax.inject.Provider

/**
 * Created by Damiano Giusti on 10/11/17.
 */
interface SymmetricEncryptor {

    fun encrypt(value: ByteArray): ByteArray?

    fun decrypt(value: ByteArray): ByteArray?
}

@Suppress("FunctionName")
fun SymmetricEncryptor(
    api23: Provider<Api23SymmetricEncryptor>,
    legacy: Provider<LegacySymmetricEncryptor>
): SymmetricEncryptor =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) api23.get()
    else legacy.get()