package com.dhug.example.utils

import com.dhug.example.data.local.entities.RemoteConfig
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.google.gson.GsonBuilder
import timber.log.Timber
import javax.inject.Singleton

@Singleton
object MMKVUtils {
    private const val IS_SKIP_INTRO = "IS_SKIP_INTRO"
    private const val IS_FIRST_LOSING_DATA = "IS_FIRST_LOSING_DATA"
    private const val IS_FIRST_FILE_NOT_LOST = "IS_FIRST_FILE_NOT_LOST"
    private const val IS_SKIP_PRIVACY = "IS_SKIP_PRIVACY"
    private const val IS_FIRST_PERMISSION = "IS_FIRST_PERMISSION"
    private const val TRACKING_SETTING_OBJECT = "TRACKING_SETTING_OBJECT"
    private const val LOCATION_OBJECT = "LOCATION_OBJECT"
    private const val REMOTE_CONFIG_OBJECT = "REMOTE_CONFIG_OBJECT"
    private const val PREMIUM_STATUS = "premium_status_"
    private const val IS_SHOW_DIALOG_PREMIUM = "IS_SHOW_DIALOG_PREMIUM"

    private const val IS_FIRST_TIME_CREATE_DB = "IS_FIRST_TIME_CREATE_DB"

    private const val IS_SKIP_ADD_CAR_LOCATION = "IS_SKIP_ADD_CAR_LOCATION"
    private const val IS_SKIP_NOT_FOUND_NOTI = "IS_SKIP_NOT_FOUND_NOTI"
    private const val IS_SKIP_FOUND_NOTI = "IS_SKIP_FOUND_NOTI"
    private const val COUNT_SHOW_NOTIFICATION_ADD_REMINDER = "COUNT_SHOW_NOTIFICATION_ADD_REMINDER"

    private const val IS_FIRST_CREATE_VEHICLE = "IS_FIRST_CREATE_VEHICLE"
    private const val IS_FIRST_ACCESS_HOME_AC = "IS_FIRST_ACCESS_HOME_AC"

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

    fun setSkipIntro(isSkip: Boolean) {
        mmkv.encode(IS_SKIP_INTRO, isSkip)
    }

    fun isSkipFlowAddCarAndLocation(): Boolean {
        return mmkv.decodeBool(IS_SKIP_ADD_CAR_LOCATION)
    }

    fun setSkipFlowAddCarAndLocation(isSkip: Boolean) {
        mmkv.encode(IS_SKIP_ADD_CAR_LOCATION, isSkip)
    }

    fun numberOfClickNotification(): Int {
        return mmkv.decodeInt(COUNT_SHOW_NOTIFICATION_ADD_REMINDER)
    }

    fun setNumberOfClickNotification(number: Int) {
        mmkv.encode(COUNT_SHOW_NOTIFICATION_ADD_REMINDER, number)
    }

    fun isFirstsAccessHome(): Boolean {
        return mmkv.decodeBool(IS_FIRST_ACCESS_HOME_AC)
    }

    fun setFirstsAccessHome(isFirst: Boolean) {
        mmkv.encode(IS_FIRST_ACCESS_HOME_AC, isFirst)
    }

    fun isFirstsCreateVehicle(): Boolean {
        return mmkv.decodeBool(IS_FIRST_CREATE_VEHICLE)
    }

    fun setFirstsCreateVehicle(isFirst: Boolean) {
        mmkv.encode(IS_FIRST_CREATE_VEHICLE, isFirst)
    }

    fun isSkipNotFoundNotify(): Boolean {
        return mmkv.decodeBool(IS_SKIP_NOT_FOUND_NOTI)
    }

    fun setSkipNotFoundNotify(isFlag: Boolean) {
        mmkv.encode(IS_SKIP_NOT_FOUND_NOTI, isFlag)
    }

    fun isSkipFoundNotify(): Boolean {
        return mmkv.decodeBool(IS_SKIP_FOUND_NOTI)
    }

    fun setSkipFoundNotify(isFlag: Boolean) {
        mmkv.encode(IS_SKIP_FOUND_NOTI, isFlag)
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