package com.molo17.commons.android.encryption.symmetric

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Created by Damiano Giusti on 2019-06-12.
 */
@Singleton
class LegacySymmetricEncryptor @Inject constructor(override val persister: IvPersister) : BaseSymmetricEncryptor() {

    companion object {
        private const val KEY_LENGTH = 256
        private val HASH_ITERATIONS = 2.0.pow(16).toInt()
    }

    private val cipher by lazy { Cipher.getInstance(ENCRYPTION_ALGORITHM) }

    private val secretKey by lazy {
        val iv = getIv(cipher.blockSize)
        // https://security.stackexchange.com/a/47188/79148
        // We use PBKDF2 because android doesn't natively support bcrypt, scrypt, or argon2i.
        // Additionally, keep in mind that this will only provide 20 bytes of security instead
        // of 32 bytes because we're using HmacSHA1. Android doesn't support HmacSHA256.
        // You can use SpongyCastle/BouncyCastle to include support for PBKDF2-HmacSHA256.
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val key = UUID.nameUUIDFromBytes(iv).toString().toCharArray()
        val spec = PBEKeySpec(key, iv, HASH_ITERATIONS, KEY_LENGTH)
        factory.generateSecret(spec)
    }

    override fun encrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(getIv(cipher.blockSize)))
        cipher.doFinal(value)
    }

    override fun decrypt(value: ByteArray): ByteArray? = synchronized(cipher) {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(getIv(cipher.blockSize)))
        cipher.doFinal(value)
    }
}