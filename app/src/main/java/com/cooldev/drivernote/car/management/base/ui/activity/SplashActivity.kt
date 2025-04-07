package com.dhug.example.base.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.view.isVisible
import com.dhug.example.AppApplication
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import com.dhug.example.base.AppActivity
import com.dhug.example.databinding.ActivitySplashBinding
import com.dhug.example.presentation.view.activity.HomeActivity
import com.dhug.example.utils.MMKVUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@AndroidEntryPoint
class SplashActivity : AppActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun getLayoutView(): View {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun isHasInterstitialAd(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openAdManager.updateCurrentActivity(null)
    }

    override fun initView() {
        waitForAdInitialization()
    }

    private fun waitForAdInitialization() {
        val app = application as AppApplication
        CoroutineScope(Dispatchers.Main).launch {
            app.isAdInitialized.collect { isInitialized ->
                if (isInitialized) {

                    if (!MMKVUtils.getRemoteConfig().isShowSplash || MMKVUtils.areAnyPremiumsActive()) {
                        flowGoToHome()
                    } else {
                        openAdManager.showAdIfAvailableFirst(this@SplashActivity) {
                            flowGoToHome()
                        }
                    }
                    showLoading(false)
                } else {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
//        binding.pbLoading.isVisible = isShow
    }

    private fun flowGoToHome(delay: Long = 0L) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(delay)
            if (MMKVUtils.isSkipIntro()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else {
//                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                finish()
            }
        }
    }

    override fun initData() {
        //
    }

    override fun observerData() {
        //
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
            //Hide status bar and navigation bar
            .hideBar(BarHide.FLAG_HIDE_BAR)
    }

    override fun initActivity() {
        // Problems and solutions: https://www.cnblogs.com/net168/p/5722752.html
        // If the current Activity is not the first Activity in the task stack
        if (!isTaskRoot) {
            val intent: Intent? = intent
            // If the current Activity is started through the desktop icon
            if (((intent != null) && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                        && (Intent.ACTION_MAIN == intent.action))
            ) {
                // Destroy the current Activity to avoid repeated instantiation entries
                finish()
                return
            }
        }
        super.initActivity()
    }
}