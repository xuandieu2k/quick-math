package com.dhug.base

import com.cooldev.base.BuildConfig

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 05 / 01 / 2025
 */
object BaseConstants {

    /**
     * Block spam interstitial ads
     */
    var isInterstitialAdShowing = false

    fun getAdmobAppId(): String {
        return BuildConfig.ADMOB_APP_ID
    }

    fun getAdmobAppIdBannerOB(): String {
        return BuildConfig.ADMOB_APP_ID_BANNER_OB
    }

    fun getAdmobAppIdInterstitial(): String {
        return BuildConfig.ADMOB_APP_ID_INTERSTITIAL
    }

    fun getAdmobAppIdBanner(): String {
        return BuildConfig.ADMOB_APP_ID_BANNER
    }

    fun getAdmobAppIdOpenAd(): String {
        return BuildConfig.ADMOB_APP_ID_OPEN_AD
    }
}