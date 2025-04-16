package com.dhug.quick_math.utils

import com.dhug.quick_math.data.local.entities.RemoteConfig
import com.dhug.quick_math.data.local.entities.Setting
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Singleton

@Singleton
object MMKVUtils {
    private const val IS_SKIP_INTRO = "IS_SKIP_INTRO"
    private const val REMOTE_CONFIG_OBJECT = "REMOTE_CONFIG_OBJECT"
    private const val SETTING_OBJECT = "SETTING_OBJECT"
    private const val PREMIUM_STATUS = "premium_status_"
    private const val IS_SHOW_DIALOG_PREMIUM = "IS_SHOW_DIALOG_PREMIUM"

    private const val IS_FIRST_TIME_CREATE_DB = "IS_FIRST_TIME_CREATE_DB"
    private const val LANGUAGE_CODE = "LANGUAGE_CODE"

    /**
     * 292 triệu năm kể từ Unix epoch (năm 1970)
     * example: 100L * 365 * 24 * 60 * 60 * 1000 // 100 năm (tính theo mili giây)
     *
     */
    const val MAX_EXPIRY_TIME = 9223372036854775807

    private val mmkv = MMKV.defaultMMKV()

    fun isSkipIntro(): Boolean {
        return mmkv.decodeBool(IS_SKIP_INTRO)
    }


    private val _languageFlow = MutableStateFlow(
        mmkv.getString(LANGUAGE_CODE, LanguageConstants.VN) ?: LanguageConstants.VN
    )
    val languageFlow: StateFlow<String> = _languageFlow.asStateFlow()


    private val _settingFlow = MutableStateFlow(getCurrentSetting())
    val settingFlow: StateFlow<Setting> = _settingFlow.asStateFlow()

    fun setLanguage(code: String) {
        mmkv.putString(LANGUAGE_CODE, code)
        _languageFlow.value = code
    }

    fun getLanguage(): String =
        mmkv.getString(LANGUAGE_CODE, LanguageConstants.VN) ?: LanguageConstants.VN

    fun saveSetting(setting: Setting) {
        val jsonSetting = Gson().toJson(setting)
        mmkv.encode(SETTING_OBJECT, jsonSetting)
        _settingFlow.value = setting
    }

    fun getCurrentSetting(): Setting {
        val strLocation = mmkv.decodeString(SETTING_OBJECT)
        return if (strLocation != null) {
            Gson().fromJson(strLocation, Setting::class.java)
        } else {
            Setting()
        }
    }


    fun saveRemoteConfig(remoteConfig: RemoteConfig?) {
        val jsonSetting = Gson().toJson(remoteConfig)
        mmkv.encode(REMOTE_CONFIG_OBJECT, jsonSetting)
    }

    fun getRemoteConfig(): RemoteConfig {
        val strLocation = mmkv.decodeString(REMOTE_CONFIG_OBJECT)
        return if (strLocation != null) {
            Gson().fromJson(strLocation, RemoteConfig::class.java)
        } else {
            RemoteConfig()
        }
    }

    /**
     * PREMIUM
     */
    fun savePremiumStatus(productId: String, expiryTime: Long) {
        Timber.tag("Save Premium Status").d("$PREMIUM_STATUS$productId $expiryTime")
        val expriTime = if (isLifetime(productId)) MAX_EXPIRY_TIME else expiryTime
        Timber.tag("Save Premium Status").d("new $PREMIUM_STATUS$productId $expriTime")
        mmkv.encode("$PREMIUM_STATUS$productId", expriTime)
    }

    fun isPremiumActive(productId: String): Boolean {
        val expiryTime = mmkv.decodeLong("$PREMIUM_STATUS$productId", 0)
        return System.currentTimeMillis() < expiryTime
    }

    fun clearPremiumStatus(productId: String) {
        mmkv.removeValueForKey("$PREMIUM_STATUS$productId")
    }

    fun clearExpiredPremiums() {
        val currentTime = System.currentTimeMillis()
        val allKeys = mmkv.allKeys() ?: emptyArray()

        for (key in allKeys) {
            if (key.startsWith(PREMIUM_STATUS)) {
                val expiryTime = mmkv.decodeLong(key, 0)
                if (expiryTime <= currentTime) {
                    Timber.tag("Remove Premium Data").d("$key ")
                    mmkv.removeValueForKey(key) // Xóa gói đã hết hạn
                }
            }
        }
    }

    fun areAnyPremiumsActive(): Boolean {
        val currentTime = System.currentTimeMillis()
        val allKeys = mmkv.allKeys() ?: emptyArray()

        for (key in allKeys) {
            if (key.startsWith(PREMIUM_STATUS)) {
                val expiryTime = mmkv.decodeLong(key, 0)
                if (expiryTime > currentTime) {
                    return true // Có ít nhất một gói còn hạn
                }
            }
        }
        return false // Không có gói nào còn hạn
    }

    fun getAllActivePremiums(): List<Pair<String, Long>> {
        val activePremiums = mutableListOf<Pair<String, Long>>()
        val allKeys = mmkv.allKeys() ?: emptyArray()
        val currentTime = System.currentTimeMillis()

        for (key in allKeys) {
            if (key.startsWith(PREMIUM_STATUS)) {
                val productId = key.removePrefix(PREMIUM_STATUS)
                val expiryTime = mmkv.decodeLong(key, 0)
                if (expiryTime > currentTime) {
                    activePremiums.add(productId to expiryTime)
                }
            }
        }
        Timber.tag("Log Package:").d(GsonBuilder().setPrettyPrinting().create().toJson(allKeys))
        Timber.tag("Log Package")
            .d(GsonBuilder().setPrettyPrinting().create().toJson(activePremiums))
        return activePremiums
    }

    fun clearAllActivePremiums() {
        val allKeys = mmkv.allKeys() ?: emptyArray()

        for (key in allKeys) {
            if (key.startsWith(PREMIUM_STATUS)) {
                mmkv.remove(key)
                Timber.tag("Log Package: Remove key").d("Key: $key")
            }
        }
    }

    fun isLifetime(productId: String): Boolean {
        return productId == PayWallConstants.LIFETIME
    }

    fun isShowedDialogPremium(): Boolean {
        return mmkv.decodeBool(IS_SHOW_DIALOG_PREMIUM)
    }

    fun setShowedDialogPremium(isShowed: Boolean) {
        mmkv.encode(IS_SHOW_DIALOG_PREMIUM, isShowed)
    }

    fun isFirstTimeCreateDb(): Boolean = mmkv.decodeBool(IS_FIRST_TIME_CREATE_DB, true)

    fun setFirstTimeCreateDb(isFirstTime: Boolean) =
        mmkv.encode(IS_FIRST_TIME_CREATE_DB, isFirstTime)

    fun isShowBannerAd(): Boolean {
        if (areAnyPremiumsActive()) return false
        return getRemoteConfig().isShowAdsBanner
    }


}