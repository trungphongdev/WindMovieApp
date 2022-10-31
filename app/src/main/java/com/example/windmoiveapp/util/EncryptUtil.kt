package com.example.windmoiveapp.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object EncryptUtil {
    //for each session, end of session, all keys are cleared
    const val SESSION_ID = "SESSION_ID"
    const val PASSWORD_SESSION = "PASSWORD_SESSION"
    const val ACCOUNT_NO_SESSION = "ACCOUNT_NO_SESSION"
    const val RANDOM_NO_SESSION = "RANDOM_NO_SESSION"
    const val PUBLIC_KEY_SESSION = "PUBLIC_KEY_SESSION"
    const val ACCOUNT_TYPE_SESSION = "ACCOUNT_TYPE_SESSION"
    const val DEVICE_ID = "DEVICE_ID"
    const val KEY_USER_ID = "KEY_USER_ID"
    const val KEY_TEMP_PASS = "KEY_TEMP_PASS"
    const val KEY_CLIENT_ID = "KEY_CLIENT_ID"
    const val KEY_CHAT_TOKEN = "KEY_CHAT_TOKEN"
    const val PROFILE_COMMUMNITY = "PROFILE_COMMUMNITY"
    const val KEY_SUGGEST_ENABLE_NOTIFICATION = "SUGGEST_ENABLE_NOTIFICATION"

    //for login
    const val ACCOUNT_NO_REMEMBER = "ACCOUNT_NO_REMEMBER"

    //for touchID
    const val LOGIN_DATA_TOUCH_ID = "ACCOUNT_NO_TOUCH_ID"
    const val PASSWORD_TOUCH_ID = "PASSWORD_TOUCH_ID"
    const val KEY_VALID_USER_COMMUNITY = "KEY_VALID_USER_COMMUNITY"

    private const val NAME_SHARE_PREF_FILE = "NAME_SHARE_PREF_FILE"
    private var pref: SharedPreferences? = null

    /*fun encryptPassWord(
        context: Context,
        password: String?,
        touchIDToken: String? = null,
        publicKey : String? = null,
        randomNo : String? = null
    ): String? {
        val snoClient = SncE2EEClient()
        var encryptedPin: String? = null
        val pubKey = publicKey ?: decryptData(context, PUBLIC_KEY_SESSION)
        val randomNoNum = randomNo ?:  decryptData(context, RANDOM_NO_SESSION)
        try {
            encryptedPin = when {
                password.toString().isEmpty() -> ""
               // touchIDToken.isNullOrEmpty() -> snoClient.encryptPIN1(pubKey, randomNoNum, password)
                else -> snoClient.encryptPIN2(pubKey, randomNoNum, password, touchIDToken)
            }
        } catch (e: Exception) {
            Log.getStackTraceString(e)
        }
        return encryptedPin
    }*/

    fun encryptPassWordTouchId(
        context: Context,
        touchIDToken: String? = null,
        publicKey: String? = null,
        randomNo: String? = null
    ): String? {
        //val snoClient = SncE2EEClient()
        var encryptedPin: String? = null
        val pubKey = publicKey ?: decryptData(context, PUBLIC_KEY_SESSION)
        val randomNumber = randomNo ?: decryptData(context, RANDOM_NO_SESSION)
        try {
           // encryptedPin =
               // snoClient.encryptPIN1(pubKey, randomNumber, touchIDToken)
        } catch (e: Exception) {
            Log.getStackTraceString(e)
        }
        return encryptedPin
    }

    fun encryptData(context: Context, key: String?, value: String?) {
        getMasterPref(context)
            .edit()
            .putString(key, value)
            .apply()
    }

    //when logout or open app.
    fun clearWorkSession(context: Context) {
        PrefUtil.getInstance().removeKey(KEY_VALID_USER_COMMUNITY)
        getMasterPref(context)
            .edit()
            .remove(SESSION_ID)
            .remove(PASSWORD_SESSION)
            .remove(ACCOUNT_NO_SESSION)
            .remove(KEY_USER_ID)
            .remove(KEY_TEMP_PASS)
            .remove(KEY_CLIENT_ID)
            .remove(RANDOM_NO_SESSION)
            .remove(PUBLIC_KEY_SESSION)
            .remove(ACCOUNT_TYPE_SESSION)
            .remove(KEY_CHAT_TOKEN)
            .apply()
    }

    fun removeData(context: Context, key: String) {
        getMasterPref(context)
            .edit()
            .remove(key)
            .apply()
    }

    fun decryptData(context: Context, key: String) =
        getMasterPref(context).getString(key, null)

    fun getMasterPref(context: Context): SharedPreferences {
        if (pref == null) {
  /*          val mainKey =  MasterKey.Builder(applicationContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()*/
            val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            pref = EncryptedSharedPreferences.create(
                NAME_SHARE_PREF_FILE,
                masterKey,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        return pref!!
    }

    fun <T> decryptData(
        context: Context,
        key: String,
        returnType: T? = null
    ): Any? {
        val mp = getMasterPref(context)
        return when (returnType) {
            is Int -> mp.getInt(key, -1)
            is Boolean -> mp.getBoolean(key, false)
            is String -> mp.getString(key, "")
            is Float -> mp.getFloat(key, -1F)
            is Long -> mp.getLong(key, -1L)
            is Set<*> -> mp.getStringSet(key, setOf())
            else -> null
        }
    }

    fun <T> encryptData(context: Context, key: String?, value: T?) {
        val mp = getMasterPref(context).edit()
        when (value) {
            is Int -> mp.putInt(key, value)
            is Boolean -> mp.putBoolean(key, value)
            is String -> mp.putString(key, value)
            is Float -> mp.putFloat(key, value)
            is Long -> mp.putLong(key, value)
            is Set<*> -> mp.putStringSet(key, value as? Set<String>)
            null -> mp.remove(key)
        }
        mp.apply()
    }
}

fun String.convertToSha1(): String {
    return this.encryptSha1()
//    return Hashing.sha1().hashString(this, Charsets.UTF_8).toString() // Guava
   // return DigestUtils.sha1Hex(this) // Apache commons <- recommended
}

fun String.encryptSha1(): String {
    var sha1: String = ""
    try {
        val crypt: MessageDigest = MessageDigest.getInstance("SHA-1")
        crypt.reset()
        crypt.update(this.toByteArray(Charsets.UTF_8))
        sha1 = crypt.digest().toHexString()
    } catch (e: NoSuchAlgorithmException) {
        Timber.e(e)
    } catch (e: UnsupportedEncodingException) {
        Timber.e(e)
    }
    return sha1
}

private fun ByteArray.toHexString(): String {
    val formatter = Formatter()
    for (b in this) {
        formatter.format("%02x", b)
    }
    val result: String = formatter.toString()
    formatter.close()
    return result
}


