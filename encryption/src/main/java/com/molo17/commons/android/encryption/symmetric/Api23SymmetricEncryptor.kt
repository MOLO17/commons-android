package com.molo17.commons.android.encryption.symmetric

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyProperties
import com.molo17.commons.android.encryption.KeysHelper
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Damiano Giusti on 2019-06-12.
 */

private const val SYMMETRIC_KEY_ALIAS = "com.molo17.aes"

@TargetApi(Build.VERSION_CODES.M)
@Singleton
class Api23SymmetricEncryptor @Inject constructor(
    private val keysHelper: KeysHelper,
    override val persister: IvPersister
) : BaseSymmetricEncryptor() {

    private val key by lazy {
        keysHelper.loadKeyStore()
        keysHelper.getSymmetricKey(SYMMETRIC_KEY_ALIAS) ?: keysHelper.generateSecretKey(
            SYMMETRIC_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
    }

    private val cipher by lazy { Cipher.getInstance(ENCRYPTION_ALGORITHM) }

    override fun encrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(getIv()))
        cipher.doFinal(value)
    }

    override fun decrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(getIv()))
        cipher.doFinal(value)
    }
}