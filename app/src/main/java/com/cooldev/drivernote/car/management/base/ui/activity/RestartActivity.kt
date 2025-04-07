package com.dhug.example.base.ui.activity

import android.app.Activity
import android.content.*
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import com.dhug.example.base.AppActivity
import com.dhug.example.databinding.ActivityRestartBinding
import com.dhug.example.presentation.view.activity.HomeActivity

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@AndroidEntryPoint
class RestartActivity : AppActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RestartActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        fun restart(context: Context) {
            val intent: Intent = if (true) {
                //If you are not logged in, jump to the splash screen page
                Intent(context, SplashActivity::class.java)
            } else {
                //If you are logged in, jump to the homepage
                Intent(context, HomeActivity::class.java)
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isHasInterstitialAd(): Boolean {
        return false
    }

    override fun getLayoutView(): View {
        return ActivityRestartBinding.inflate(layoutInflater).root
    }

    override fun initView() {}

    override fun initData() {
        restart(this)
        finish()
//        toast(R.string.common_crash_hint)
    }

    override fun observerData() {
        //
    }
}