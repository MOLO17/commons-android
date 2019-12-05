package com.molo17.commons.android.encryption.asymmetric

import android.os.Build
import javax.inject.Provider

/**
 * Created by Damiano Giusti on 10/11/17.
 */
interface AsymmetricEncryptor {

    fun encrypt(value: ByteArray): ByteArray?

    fun decrypt(value: ByteArray): ByteArray?
}

@Suppress("FunctionName")
fun AsymmetricEncryptor(
    api23: Provider<Api23AsymmetricEncryptor>,
    legacy: Provider<LegacyAsymmetricEncryptor>
): AsymmetricEncryptor =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) api23.get()
    else legacy.get()