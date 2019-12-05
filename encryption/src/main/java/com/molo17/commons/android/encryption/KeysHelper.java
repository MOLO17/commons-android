package com.molo17.commons.android.encryption;

import android.annotation.TargetApi;
import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.Nullable;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Damiano Giusti on 10/11/17.
 */
public class KeysHelper {

    private KeyStore keyStore;

    KeysHelper(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @Inject
    public KeysHelper() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Loads the {@link KeyStore} associated with this instance.
     *
     * @throws IllegalStateException if the operation fails.
     */
    public void loadKeyStore() {
        try {
            keyStore.load(null);
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Generates a new {@link KeyPair} with the given alias.
     *
     * @param context Context used for the key generation
     * @param alias   Alias of the key pair which will be generated.
     * @return the generated {@link KeyPair}
     * @throws GeneralSecurityException if the generation process fails.
     * @deprecated Use {@link #generateKeyPair(String, int)} instead.
     */
    @Deprecated
    public KeyPair generateKeyPair(Context context, String alias) throws GeneralSecurityException {
        final Calendar start = new GregorianCalendar();
        final Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 100);

        AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();

        final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        gen.initialize(spec);
        return gen.generateKeyPair();
    }

    /**
     * Generates a new {@link KeyPair} with the given alias.
     *
     * @param alias    Alias of the key pair which will be generated.
     * @param purposes Purposes of the generated keys.
     * @return the generated {@link KeyPair}
     * @throws GeneralSecurityException if the generation process fails.
     */
    @TargetApi(23)
    public KeyPair generateKeyPair(String alias, int purposes) throws GeneralSecurityException {
        final Calendar start = new GregorianCalendar();
        final Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 100);

        AlgorithmParameterSpec spec = new KeyGenParameterSpec.Builder(alias, purposes)
                .setCertificateSubject(new X500Principal("CN=" + alias))
                .setCertificateSerialNumber(BigInteger.ONE)
                .setKeyValidityStart(start.getTime())
                .setKeyValidityEnd(end.getTime())
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build();

        final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        gen.initialize(spec);
        return gen.generateKeyPair();
    }

    @TargetApi(23)
    public SecretKey generateSecretKey(String alias, int purposes) throws GeneralSecurityException {

        AlgorithmParameterSpec spec = new KeyGenParameterSpec.Builder(alias, purposes)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(spec);
        return keyGenerator.generateKey();
    }

    /**
     * Returns the asymmetric {@link KeyPair} associated with the given alias.
     *
     * @param alias the alias which represents the key pair.
     * @return the {@link KeyPair} instance, null if none.
     */
    @Nullable
    public KeyPair getAsymmetricKeys(String alias) {
        try {
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            if (entry != null) {
                return new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey());
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            return null;
        }
    }

    /**
     * Deletes from the KeyStore the {@link KeyPair} associated with the given alias.
     */
    public boolean deleteKeyPair(String alias) {
        try {
            keyStore.deleteEntry(alias);
            return true;
        } catch (KeyStoreException e) {
            return false;
        }
    }

    @Nullable
    public SecretKey getSymmetricKey(String alias) {
        try {
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);
            if (entry != null) {
                return entry.getSecretKey();
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            return null;
        }
    }

    public boolean deleteSymmetricKey(String alias) {
        try {
            keyStore.deleteEntry(alias);
            return true;
        } catch (KeyStoreException e) {
            return false;
        }
    }
}
