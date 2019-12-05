package com.molo17.commons.android.encryption.asymmetric

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyProperties
import com.molo17.commons.android.encryption.KeysHelper
import java.security.KeyPair
import javax.inject.Inject

/**
 * Created by Damiano Giusti on 2019-06-12.
 */

@TargetApi(Build.VERSION_CODES.M)
class Api23AsymmetricEncryptor @Inject constructor(
    private val keysHelper: KeysHelper
) : BaseAsymmetricEncryptor() {

    override val keys: KeyPair by lazy {
        keysHelper.getAsymmetricKeys(ASYMMETRIC_KEY_ALIAS) ?: keysHelper.generateKeyPair(
            ASYMMETRIC_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
    }
}