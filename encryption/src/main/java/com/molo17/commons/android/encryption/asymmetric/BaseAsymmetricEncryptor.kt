package com.molo17.commons.android.encryption.asymmetric

import java.security.KeyPair
import javax.crypto.Cipher

/**
 * Created by Damiano Giusti on 2019-06-12.
 */

abstract class BaseAsymmetricEncryptor : AsymmetricEncryptor {

    companion object {
        const val ASYMMETRIC_KEY_ALIAS = "com.molo17.rsa"
        const val ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding"
    }

    abstract val keys: KeyPair

    private val cipher by lazy { Cipher.getInstance(ENCRYPTION_ALGORITHM) }

    override fun encrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.ENCRYPT_MODE, keys.public)
        cipher.doFinal(value)
    }

    override fun decrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.DECRYPT_MODE, keys.private)
        cipher.doFinal(value)
    }
}