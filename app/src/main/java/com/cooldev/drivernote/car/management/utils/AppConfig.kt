package com.dhug.example.utils

import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 31 / 12 / 2024
 */
@Singleton
object AppConfig {

    fun isLogEnable(): Boolean {
        return com.dhug.example.BuildConfig.LOG_ENABLE
    }

    fun getPackageName(): String {
        return com.dhug.example.BuildConfig.APPLICATION_ID
    }

    fun getVersionName(): String {
        return com.dhug.example.BuildConfig.VERSION_NAME
    }

    fun isDebug(): Boolean {
        return com.dhug.example.BuildConfig.BUILD_TYPE == "DEBUG"
    }
}