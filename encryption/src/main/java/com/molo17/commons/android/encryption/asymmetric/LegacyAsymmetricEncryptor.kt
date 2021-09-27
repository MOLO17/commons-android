package com.molo17.commons.android.encryption.asymmetric

import android.content.Context
import com.molo17.commons.android.encryption.KeysHelper
import java.security.KeyPair
import javax.inject.Inject

/**
 * Created by Damiano Giusti on 2019-06-12.
 */

@Suppress("DEPRECATION")
class LegacyAsymmetricEncryptor @Inject constructor(
    private val context: Context,
    private val keysHelper: KeysHelper
) : BaseAsymmetricEncryptor() {

    override val keys: KeyPair by lazy {
        keysHelper.getAsymmetricKeys(ASYMMETRIC_KEY_ALIAS) ?: keysHelper.generateKeyPair(context, ASYMMETRIC_KEY_ALIAS)
    }
}