package com.dhug.quick_math.utils

import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 31 / 12 / 2024
 */
@Singleton
object AppConfig {

    fun isLogEnable(): Boolean {
        return com.dhug.quick_math.BuildConfig.LOG_ENABLE
    }

    fun getPackageName(): String {
        return com.dhug.quick_math.BuildConfig.APPLICATION_ID
    }

    fun getVersionName(): String {
        return com.dhug.quick_math.BuildConfig.VERSION_NAME
    }

    fun isDebug(): Boolean {
        return com.dhug.quick_math.BuildConfig.BUILD_TYPE == "DEBUG"
    }
}