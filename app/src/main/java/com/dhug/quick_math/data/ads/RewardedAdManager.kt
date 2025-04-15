package com.dhug.quick_math.data.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import timber.log.Timber

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    RewardedAdManager.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 09:31
 * Description: File RewardedAdManager.kt created by admin - 12/4/25 at 09:31
 */
object RewardedAdManager {

    private var rewardedAd: RewardedAd? = null
    private var isLoading = false

    fun loadRewardedAd(context: Context, adUnitId: String, onLoaded: (() -> Unit)? = null, onFailed: (() -> Unit)? = null) {
        if (isLoading || rewardedAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                Timber.d("Rewarded ad loaded.")
                rewardedAd = ad
                isLoading = false
                onLoaded?.invoke()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Timber.e("Failed to load rewarded ad: ${loadAdError.message}")
                rewardedAd = null
                isLoading = false
                onFailed?.invoke()
            }
        })
    }

    fun showRewardedAd(activity: Activity, onRewardEarned: () -> Unit, onAdClosed: (() -> Unit)? = null) {
        val ad = rewardedAd
        if (ad != null) {
            ad.show(activity) { rewardItem: RewardItem ->
                Timber.d("User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onRewardEarned()
            }

            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Timber.d("Rewarded ad dismissed.")
                    rewardedAd = null
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Timber.e("Ad failed to show: ${adError.message}")
                    rewardedAd = null
                    onAdClosed?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Timber.d("Ad showed fullscreen content.")
                }
            }

        } else {
            Timber.d("The rewarded ad wasn't ready yet.")
            onAdClosed?.invoke()
        }
    }

    fun isAdAvailable(): Boolean = rewardedAd != null
}