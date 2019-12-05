package com.molo17.commons.android.encryption.symmetric

import java.security.SecureRandom

/**
 * Created by Damiano Giusti on 2019-06-12.
 */
abstract class BaseSymmetricEncryptor : SymmetricEncryptor {

    companion object {
        const val ENCRYPTION_ALGORITHM = "AES/CBC/PKCS7PADDING"
    }

    abstract val persister: IvPersister

    interface IvPersister {
        fun saveInitializationVector(iv: ByteArray)
        fun getInitializationVector(): ByteArray?
    }

    protected fun getIv(length: Int = 16) =
        persister.getInitializationVector() ?: ByteArray(length)
            .apply { SecureRandom().nextBytes(this) }
            .also { persister.saveInitializationVector(it) }
}