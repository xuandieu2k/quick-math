package com.dhug.quick_math.data.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.dhug.base.BaseConstants
import com.dhug.quick_math.utils.MMKVUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAdManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val application: Application
) :
    Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isAdShowing = false
    private var lastLoadTime: Long = 0
    private var currentActivity: Activity? = null

    companion object {
        const val TAG_LOG = "OPEN_AD"
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(this)
    }

    // Load ads Open Ad
    fun loadAd() {
        if (MMKVUtils.areAnyPremiumsActive()) return
        if (appOpenAd == null) {
            Timber.tag(TAG_LOG).d("Loading Open Ad...")
            val adRequest = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                BaseConstants.getAdmobAppIdOpenAd(),
                adRequest,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        lastLoadTime = System.currentTimeMillis()
                        Timber.tag(TAG_LOG).d("Open Ad loaded successfully.")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        appOpenAd = null
                        Timber.tag(TAG_LOG).e("Failed to load Open Ad: ${error.message}")
                    }
                }
            )
        } else {
            Timber.tag(TAG_LOG).d("Open Ad already loaded.")
        }
    }

    // Show Open Ad if available and app returns from background
    fun showAdIfAvailable(activity: Activity, onAdDismissed: () -> Unit) {
        if (MMKVUtils.areAnyPremiumsActive()) {
            resetOpenAd()
            return
        }
        if (appOpenAd == null) {
            Timber.tag(TAG_LOG).d("No Open Ad available to show.")
            onAdDismissed()
            return
        }

        if (isAdExpired()) {
            Timber.tag(TAG_LOG).d("Open Ad expired. Loading a new ad.")
            appOpenAd = null
            loadAd()
            onAdDismissed()
            return
        }

        if (!isAdShowing) {
            Timber.tag(TAG_LOG).d("Showing Open Ad...")
            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    showSystemUI()
                    appOpenAd = null
                    isAdShowing = false
                    Timber.tag(TAG_LOG).d("Open Ad dismissed.")
                    onAdDismissed()
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    showSystemUI()
                    isAdShowing = false
                    Timber.tag(TAG_LOG).e("Failed to show Open Ad: ${error.message}")
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    hideSystemUI()
                    isAdShowing = true
                    Timber.tag(TAG_LOG).d("Open Ad is showing.")
                }
            }
            appOpenAd?.show(activity)
        } else {
            Timber.tag(TAG_LOG).d("Open Ad is already showing.")
        }
    }

    // Check if the ad has expired or not
    private fun isAdExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val expirationTime = 1 * 60 * 60 * 1000
        val expired = currentTime - lastLoadTime > expirationTime
        Timber.tag(TAG_LOG).d("Is Open Ad expired: $expired")
        return expired
    }

    fun loadAdFirst(activity: Activity, onAdDismissed: () -> Unit) {
        if (MMKVUtils.areAnyPremiumsActive()) return
        if (appOpenAd == null) {
            Timber.tag(TAG_LOG).d("Loading Open Ad...")
            val adRequest = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                BaseConstants.getAdmobAppIdOpenAd(),
                adRequest,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        lastLoadTime = System.currentTimeMillis()
                        initListenerOpenAd(onAdDismissed)
                        appOpenAd?.show(activity)
                        Timber.tag(TAG_LOG).d("Open Ad loaded successfully in First.")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        appOpenAd = null
                        Timber.tag(TAG_LOG).e("Failed to load Open Ad: ${error.message}")
                        onAdDismissed()
                    }
                }
            )
        } else {
            Timber.tag(TAG_LOG).d("Open Ad already loaded.")
        }
    }

    // Show Open Ad if available and app returns from background
    fun showAdIfAvailableFirst(activity: Activity, onAdDismissed: () -> Unit) {
        if (MMKVUtils.areAnyPremiumsActive()) {
            resetOpenAd()
            return
        }
        if (appOpenAd == null) {
            Timber.tag(TAG_LOG).d("No Open Ad available to show.")
            loadAdFirst(activity, onAdDismissed)
            return
        }

        if (isAdExpired()) {
            Timber.tag(TAG_LOG).d("Open Ad expired. Loading a new ad.")
            appOpenAd = null
            loadAdFirst(activity, onAdDismissed)
            onAdDismissed()
            return
        }

        if (!isAdShowing) {
            Timber.tag(TAG_LOG).d("Showing Open Ad...")
            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    showSystemUI()
                    appOpenAd = null
                    isAdShowing = false
                    Timber.tag(TAG_LOG).d("Open Ad dismissed.")
                    onAdDismissed()
                    loadAdFirst(activity, onAdDismissed)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    showSystemUI()
                    isAdShowing = false
                    Timber.tag(TAG_LOG).e("Failed to show Open Ad: ${error.message}")
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    hideSystemUI()
                    isAdShowing = true
                    Timber.tag(TAG_LOG).d("Open Ad is showing.")
                }
            }
            appOpenAd?.show(activity)
        } else {
            Timber.tag(TAG_LOG).d("Open Ad is already showing.")
        }
    }

    private fun initListenerOpenAd(onAdDismissed: () -> Unit) {
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isAdShowing = false
                Timber.tag(TAG_LOG).d("Open Ad dismissed.")
                onAdDismissed()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                isAdShowing = false
                Timber.tag(TAG_LOG).e("Failed to show Open Ad: ${error.message}")
                onAdDismissed()
            }

            override fun onAdShowedFullScreenContent() {
                isAdShowing = true
                Timber.tag(TAG_LOG).d("Open Ad is showing.")
            }
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            currentActivity?.window?.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            currentActivity?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        currentActivity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            currentActivity?.window?.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            currentActivity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        currentActivity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //
    }

    override fun onActivityStarted(activity: Activity) {
        //
    }

    override fun onActivityResumed(activity: Activity) {
        if (MMKVUtils.areAnyPremiumsActive()) {
            return
        }
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        //
    }

    override fun onActivityStopped(activity: Activity) {
        //
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //
    }

    override fun onActivityDestroyed(activity: Activity) {
        //
    }

    @Suppress("DEPRECATION")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        currentActivity?.let {
            if (!MMKVUtils.areAnyPremiumsActive()) {
                if (!MMKVUtils.getRemoteConfig().isShowSplash) return@let
                showAdIfAvailable(it) {
                    Timber.d("Open Ad dismissed.")
                }
            }
        }
    }

    fun updateCurrentActivity(activity: Activity?) {
        currentActivity = activity
    }

    fun resetOpenAd() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        currentActivity = null
        appOpenAd = null
    }

    fun registerObserver() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun getOpenAd(): AppOpenAd? {
        return appOpenAd
    }
}