package com.dhug.quick_math.utils

import android.text.TextUtils
import android.util.Base64
import com.android.billingclient.api.Purchase
import timber.log.Timber
import java.io.IOException
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.Signature
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.inject.Singleton

@Singleton
object SecurityUtils {

    fun isSignatureValid(purchase: Purchase): Boolean {
        return verifyPurchase(
            BASE_64_ENCODED_PUBLIC_KEY, purchase.originalJson, purchase.signature
        )
    }

    private const val TAG = "Security"
    private const val KEY_FACTORY_ALGORITHM = "RSA"
    private const val SIGNATURE_ALGORITHM = "SHA1withRSA"

    private const val BASE_64_ENCODED_PUBLIC_KEY = "45345"

    @Throws(IOException::class)
    fun verifyPurchase(base64PublicKey: String, signedData: String, signature: String): Boolean {
        if ((TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey)
                    || TextUtils.isEmpty(signature))
        ) {
            Timber.tag(TAG).w("Purchase verification failed: missing data.")
            return false
        }
        val key = generatePublicKey(base64PublicKey)
        return verify(key, signedData, signature)
    }

    @Throws(IOException::class)
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        try {
            val decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            return keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            // "RSA" is guaranteed to be available.
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            val msg = "Invalid key specification: $e"
            Timber.tag(TAG).w(msg)
            throw IOException(msg)
        }
    }

    private fun verify(publicKey: PublicKey, signedData: String, signature: String): Boolean {
        val signatureBytes: ByteArray
        try {
            signatureBytes = Base64.decode(signature, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Timber.tag(TAG).w("Base64 decoding failed.")
            return false
        }
        try {
            val signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM)
            signatureAlgorithm.initVerify(publicKey)
            signatureAlgorithm.update(signedData.toByteArray())
            if (!signatureAlgorithm.verify(signatureBytes)) {
                Timber.tag(TAG).w("Signature verification failed...")
                return false
            }
            return true
        } catch (e: NoSuchAlgorithmException) {
            // "RSA" is guaranteed to be available.
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            Timber.tag(TAG).w("Invalid key specification.")
        } catch (e: SignatureException) {
            Timber.tag(TAG).w("Signature exception.")
        }
        return false
    }
}