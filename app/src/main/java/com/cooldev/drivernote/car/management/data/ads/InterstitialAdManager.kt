package com.dhug.example.data.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.cooldev.base.BaseConstants
import com.dhug.example.presentation.dialog.LoadingAdDialog
import com.dhug.example.utils.MMKVUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import timber.log.Timber
import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 11 / 01 / 2025
 */
@SuppressLint("StaticFieldLeak")
@Singleton
object InterstitialAdManager : DefaultLifecycleObserver {

    private var adDisplayCount = 0
    private var maxAds = 5
    private var timeLimit = 60 * 1000L
    private var lastAdDisplayTime = 0L
    private var startTime = System.currentTimeMillis()
    private var isAdShowing = false
    private var interstitialAd: InterstitialAd? = null

    @SuppressLint("StaticFieldLeak")
    private var dialog: LoadingAdDialog.Builder? = null

    private var currentActivity: Activity? = null

    // Initialize AdManager with Application Context (without loading the ad)
    fun initialize(context: Context, maxAds: Int, timeLimitSeconds: Long) {
        if (MMKVUtils.areAnyPremiumsActive()) return
        if (maxAds <= 0) {
            this.maxAds = 0
            Timber.tag("AdManager")
                .e("AdManager initialization skipped: maxAds=$maxAds is not valid.")
            return
        }

        InterstitialAdManager.maxAds = maxAds
        timeLimit = timeLimitSeconds * 1000L

        Timber.tag("AdManager")
            .d("AdManager initialized with maxAds=$maxAds, timeLimit=$timeLimit ms")
    }

    // Load interstitial ad only when required (on show)
    private fun loadInterstitialAd(context: Context, onLoadDone: (isLoaded: Boolean) -> Unit = {}) {
        Timber.tag("AdManager Init Inter").d("Inter Ad init and loading...")
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            BaseConstants.getAdmobAppIdInterstitial(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    onLoadDone(true)
                    Timber.tag("AdManager").d("Ad loaded successfully.")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    onLoadDone(false)
                    Timber.tag("AdManager").d("Ad failed to load: ${adError.message}")
                }
            }
        )
    }

    // Show ad on click event with eligibility check
    fun showAdOnClick(activity: Activity, onDone: () -> Unit) {
        Timber.tag("AdManager").d("Ad button clicked. Checking eligibility...")
        // Proceed with the ad eligibility check and show logic
        showAdIfEligible(activity, onDone)
    }

    // Show ad if eligible
    private fun showAdIfEligible(activity: Activity, onDone: () -> Unit) {
        // Has Premium return
        if (MMKVUtils.areAnyPremiumsActive()) {
            destroyAd()
            onDone()
            return
        }
        if (maxAds <= 0) {
            Timber.tag("AdManager").e("Invalid maxAds value: $maxAds. Skipping ad check.")
            onDone()
            return
        }

        val currentTime = System.currentTimeMillis()
        val intervalBetweenAds = timeLimit / maxAds

        // Generate random interval between min and max
        val randomInterval = (intervalBetweenAds..(intervalBetweenAds * 2)).random()

        val elapsedTime = currentTime - lastAdDisplayTime

        Timber.tag("AdManager")
            .d("Checking ad eligibility... elapsedTime=$elapsedTime, randomInterval=$randomInterval")

        // Reset control if time limit has been exceeded
        if (currentTime - startTime > timeLimit) {
            Timber.tag("AdManager").d("Time limit exceeded. Resetting ad control.")
            resetAdControl()
        }

        // Check if ad is already showing or max ads reached
        if (isAdShowing || BaseConstants.isInterstitialAdShowing) {
            Timber.tag("AdManager")
                .d("Ad is already showing. isAdShowing:$isAdShowing BaseConstants.isInterstitialAdShowing:${BaseConstants.isInterstitialAdShowing}")
            onDone()
            return
        }

        if (adDisplayCount >= maxAds) {
            Timber.tag("AdManager").d("Maximum number of ads reached.")
            onDone()
            return
        }

        // Check if enough time has passed to show the ad
        if (elapsedTime >= randomInterval) {
            showDialog()
            // Load the ad only when showAdOnClick is called
            loadInterstitialAd(activity.applicationContext) {
                if (it) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (interstitialAd == null) {
                            Timber.tag("AdManager").d("Inter Ad not init")
                            onDone()
                        }
                        interstitialAd?.let { ad ->
                            if (!activity.isFinishing) {
                                isAdShowing = true
                                Timber.tag("AdManager")
                                    .d("Showing ad... adDisplayCount=$adDisplayCount")
                                ad.show(activity)

                                ad.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            hideDialog()
                                            isAdShowing = false
                                            interstitialAd = null
                                            Timber.tag("AdManager")
                                                .d("Ad dismissed. Loading new ad...")
                                            onDone()
                                        }

                                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                            hideDialog()
                                            onDone()
                                        }

                                        override fun onAdShowedFullScreenContent() {
                                            hideDialog()
                                            super.onAdShowedFullScreenContent()
                                        }
                                    }

                                adDisplayCount++
                                lastAdDisplayTime = currentTime
                            }
                        }
                    }, 1000)
                } else {
                    hideDialog()
                    // Ad failed to load
                    onDone()
                }
            }
        } else {
            hideDialog()
            Timber.tag("AdManager").d("Not enough time since last ad. Waiting...")
            onDone()
        }
    }

    // Reset the ad control state
    private fun resetAdControl() {
        startTime = System.currentTimeMillis()
        adDisplayCount = 0
        lastAdDisplayTime = startTime
        Timber.tag("AdManager").d("Ad control reset. startTime=$startTime")
    }

    // Destroy ad and release resources
    fun destroyAd() {
        interstitialAd = null
        Timber.tag("AdManager").d("Ad destroyed.")
    }

    // Lifecycle callbacks
    override fun onPause(owner: LifecycleOwner) {
        Timber.tag("AdManager").d("onPause: AdManager lifecycle callback.")
    }

    override fun onResume(owner: LifecycleOwner) {
        Timber.tag("AdManager").d("onResume: AdManager lifecycle callback.")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Timber.tag("AdManager").d("onDestroy: Cleaning up resources.")
        currentActivity = null
        dialog?.dismiss()
        dialog = null
        interstitialAd = null
    }

    fun getInterstitialAd(): InterstitialAd? {
        return interstitialAd
    }

    fun initDialog(activity: Activity) {
        try {
            if (activity == currentActivity && dialog != null) return
            currentActivity = activity
            Timber.tag("AdManager")
                .d("currentActivity: ${currentActivity?.javaClass?.name} activity: ${activity.javaClass.name}")
            currentActivity?.let {
                dialog = LoadingAdDialog.Builder(it)
            }
            dialog?.create()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun showDialog() {
        Timber.tag("AdManager").d("Show Dialog Loading Ad. $dialog")
        dialog?.show()
    }

    fun hideDialog() {
        Timber.tag("AdManager").d("Hide Dialog Loading Ad. $dialog")
        dialog?.dismiss()
    }
}