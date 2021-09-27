package com.molo17.commons.android.encryption

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * Created by Damiano Giusti on 2019-05-13.
 */
class HashHelper @Inject constructor(
    private val base64Converter: Base64Converter
) {

    fun getSha256(string: String): String {
        val digester = MessageDigest.getInstance("SHA-256")
        val hash = digester.digest(string.toByteArray())
        return base64Converter.encodeToString(hash)
    }

    fun hmac256(key: String, message: String): String {
        val algorithm = "HmacSHA256"
        val hash = Mac.getInstance(algorithm)
            .apply { init(SecretKeySpec(key.toByteArray(Charsets.UTF_8), algorithm)) }
            .doFinal(message.toByteArray(Charsets.UTF_8))
        return base64Converter.encodeToString(hash)
    }

    fun aesEncrypt(key: String, message: String): String {
        val bytes = message.toByteArray()
        val output = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            .apply { init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"), IvParameterSpec(ByteArray(16) { 0x00 })) }
            .doFinal(bytes)
        return base64Converter.encodeToString(output)
    }
}