package com.molo17.commons.android.encryption

import android.content.Context
import android.content.SharedPreferences
import com.molo17.commons.android.encryption.symmetric.Api23SymmetricEncryptor
import com.molo17.commons.android.encryption.symmetric.LegacySymmetricEncryptor
import com.molo17.commons.android.encryption.symmetric.SymmetricEncryptor
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Damiano Giusti on 2019-06-12.
 */
@Singleton
class SecurePreferences @Inject constructor(
    private val context: Context,
    private val symmetricEncryptor: SymmetricEncryptor,
    private val base64Converter: Base64Converter,
    private val hashHelper: HashHelper
) : SharedPreferences {

    private val delegate by lazy { context.getSharedPreferences("molo17", Context.MODE_PRIVATE) }

    ///////////////////////////////////////////////////////////////////////////
    // SharedPreferences
    ///////////////////////////////////////////////////////////////////////////

    override fun contains(key: String): Boolean = delegate.contains(hashHelper.getSha256(key))

    override fun getBoolean(key: String?, defValue: Boolean): Boolean =
        delegate.getBoolean(key?.let(hashHelper::getSha256), defValue)

    override fun getInt(key: String?, defValue: Int): Int =
        delegate.getInt(key?.let(hashHelper::getSha256), defValue)

    override fun getAll(): MutableMap<String, *> = delegate.all

    override fun edit(): SharedPreferences.Editor = SecureEditor(delegate.edit())

    override fun getLong(key: String?, defValue: Long): Long =
        delegate.getLong(key?.let(hashHelper::getSha256), defValue)

    override fun getFloat(key: String?, defValue: Float): Float =
        delegate.getFloat(key?.let(hashHelper::getSha256), defValue)

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        delegate.getStringSet(key?.let(hashHelper::getSha256), defValues)

    override fun getString(key: String?, defValue: String?): String? =
        delegate.getString(key?.let(hashHelper::getSha256), defValue)
            ?.let(base64Converter::decode)
            ?.let(symmetricEncryptor::decrypt)
            ?.toString(Charsets.ISO_8859_1)

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        delegate.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        delegate.unregisterOnSharedPreferenceChangeListener(listener)
    }

    ///////////////////////////////////////////////////////////////////////////
    // SharedPreferences.Editor
    ///////////////////////////////////////////////////////////////////////////

    private inner class SecureEditor(private val base: SharedPreferences.Editor) :
        SharedPreferences.Editor {

        override fun clear(): SharedPreferences.Editor {
            base.clear()
            return this
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
            base.putLong(key?.let(hashHelper::getSha256), value)
            return this
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
            base.putInt(key?.let(hashHelper::getSha256), value)
            return this
        }

        override fun remove(key: String?): SharedPreferences.Editor {
            base.remove(key?.let(hashHelper::getSha256))
            return this
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            base.putBoolean(key?.let(hashHelper::getSha256), value)
            return this
        }

        override fun putStringSet(
            key: String?,
            values: MutableSet<String>?
        ): SharedPreferences.Editor {
            base.putStringSet(key?.let(hashHelper::getSha256), values)
            return this
        }

        override fun commit(): Boolean {
            return base.commit()
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            base.putFloat(key?.let(hashHelper::getSha256), value)
            return this
        }

        override fun apply() {
            base.apply()
        }

        override fun putString(key: String?, value: String?): SharedPreferences.Editor {
            base.putString(
                key?.let(hashHelper::getSha256),
                value?.toByteArray(Charsets.ISO_8859_1)
                    ?.let(symmetricEncryptor::encrypt)
                    ?.let(base64Converter::encodeToString)
            )
            return this
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Factory
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun create(context: Context): SharedPreferences {
            val base64Converter = Base64Converter()
            val keysHelper = { KeysHelper() }
            val persister = { PrefsIvPersister(context, base64Converter) }
            return SecurePreferences(
                context = context,
                symmetricEncryptor = SymmetricEncryptor(
                    api23 = Provider { Api23SymmetricEncryptor(keysHelper(), persister()) },
                    legacy = Provider { LegacySymmetricEncryptor(persister()) }
                ),
                base64Converter = base64Converter,
                hashHelper = HashHelper(base64Converter)
            )
        }
    }
}