package com.cooldev.base

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
        return com.cooldev.base.BuildConfig.ADMOB_APP_ID
    }

    fun getAdmobAppIdBannerOB(): String {
        return com.cooldev.base.BuildConfig.ADMOB_APP_ID_BANNER_OB
    }

    fun getAdmobAppIdInterstitial(): String {
        return com.cooldev.base.BuildConfig.ADMOB_APP_ID_INTERSTITIAL
    }

    fun getAdmobAppIdBanner(): String {
        return com.cooldev.base.BuildConfig.ADMOB_APP_ID_BANNER
    }

    fun getAdmobAppIdOpenAd(): String {
        return com.cooldev.base.BuildConfig.ADMOB_APP_ID_OPEN_AD
    }
}