package com.dhug.quick_math.presentation.view.activity

import android.view.View
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.databinding.ActivitySettingBinding

class SettingActivity : AppAdsActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        //
    }

    override fun initData() {
        //
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM
}