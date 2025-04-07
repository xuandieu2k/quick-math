package com.dhug.quick_math

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.work.Configuration
import com.dhug.quick_math.base.aop.Log
import com.dhug.quick_math.base.manager.ActivityManager
import com.dhug.quick_math.base.other.DebugLoggerTree
import com.dhug.quick_math.base.other.TitleBarStyle
import com.dhug.quick_math.base.other.ToastLogInterceptor
import com.dhug.quick_math.base.other.ToastStyle
import com.dhug.quick_math.data.ads.InterstitialAdManager
import com.dhug.quick_math.domain.usecase.PurchaseUseCase
import com.dhug.quick_math.framework.alarm.AlarmScheduler
import com.dhug.quick_math.utils.AppConfig
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.MMKVUtils
import com.dhug.quick_math.utils.RemoteConfigManager
import com.google.android.gms.ads.MobileAds
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.hjq.bar.TitleBar
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager

    @Inject
    lateinit var purchaseUseCase: PurchaseUseCase

    // Support wake up
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val _isAdInitialized = MutableStateFlow(false)
    val isAdInitialized: StateFlow<Boolean> get() = _isAdInitialized

    @Log("Startup time taken")
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, getString(R.string.google_api_key)) // Search place googleMap
        FirebaseApp.initializeApp(this)
        initMMKV()
        MMKVUtils.clearExpiredPremiums()
        connectPurchase {
            refreshDataPurchase {
                fetchRemoteConfig()
            }
        }
        // init wakeup
        val alarmScheduler = AlarmScheduler(this)
        alarmScheduler.scheduleAlarm()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //Clear all image memory caches
        // GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        //Clear the image memory cache according to the remaining memory of the phone
        // GlideApp.get(this).onTrimMemory(level)
    }

    private fun fetchRemoteConfig() {
        CoroutineScope(Dispatchers.IO).launch {
            remoteConfigManager.fetchFistRemoteConfig {
                CoroutineScope(Dispatchers.Main).launch {
                    initAd(this@AppApplication)
                    initSdk(this@AppApplication)
                    _isAdInitialized.value = true
                }
            }
        }
    }

    private fun refreshDataPurchase(onDone: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            purchaseUseCase.refreshPurchases(onError = {
                onDone()
            },
                onDone = {
                    onDone()
                })
        }
    }

    private fun connectPurchase(onDone: () -> Unit) {
        purchaseUseCase.startConnection(
            onConnected = {
                Timber.Forest.tag("LOG BILLING").d("CONNECTED")
                CoroutineScope(Dispatchers.IO).launch {
                    purchaseUseCase.refreshPurchases(
                        onDone = {
                            onDone()
                        },
                        onError = {
                            onDone()
                        }
                    )
                }
            },
            onError = {
                Timber.Forest.tag("LOG BILLING").d("${it.message}")
                it.printStackTrace()
                onDone()
            }
        )
    }

    /**
     * Initialize MMKV
     */
    private fun initMMKV() {
        MMKV.initialize(this)
    }

    /**
     *
     */
    private fun initAd(application: Application) {
        // Initialize Google Mobile Ads SDK and wait for completion
        MobileAds.initialize(application) { _ ->
            Timber.Forest.d("AdManager", "Google Mobile Ads SDK initialized.")
            // Initialize AdManager after MobileAds is ready
            if (MMKVUtils.areAnyPremiumsActive()) return@initialize
            InterstitialAdManager.initialize(
                application,
                MMKVUtils.getRemoteConfig().maxInterAdsCount.toInt(),
                MMKVUtils.getRemoteConfig().interstitialInterval
            )
        }
    }

    /**
     * Initialize some third-party frameworks
     */
    private fun initSdk(application: Application) {
        TitleBar.setDefaultStyle(TitleBarStyle())

        //
        createNotificationChannels(application)
        //Initialize toast
        ToastUtils.init(application, ToastStyle())
        //Set debug mode
        // Set up Toast interceptor
        ToastUtils.setInterceptor(ToastLogInterceptor())

        //Local exception capture
//        CrashHandler.register(application)

        //Activity stack management initialization
        ActivityManager.Companion.getInstance().init(application)

        //Initialize log printing
        if (AppConfig.isLogEnable()) {
            Timber.Forest.plant(DebugLoggerTree())
        }

        //Register network status change monitoring
        val connectivityManager: ConnectivityManager? =
            ContextCompat.getSystemService(application, ConnectivityManager::class.java)
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    val topActivity: Activity? = ActivityManager.Companion.getInstance().getTopActivity()
                    if (topActivity !is LifecycleOwner) {
                        return
                    }
                    val lifecycleOwner: LifecycleOwner = topActivity
                    if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                        return
                    }
                    ToastUtils.show(R.string.common_network_error)
                }

                override fun onAvailable(network: Network) {
                    connectPurchase {
                        //
                    }
                }
            })
        }

//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build()
//            )
//            StrictMode.setVmPolicy(
//                StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build()
//            )
//        }

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val reminderChanel = NotificationChannel(
                AppConstants.CHANEL_ID_NOTIFICATION_REMINDER,
                AppConstants.CHANEL_NAME_NOTIFICATION_REMINDER,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(reminderChanel))
        }
    }

}