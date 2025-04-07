package com.dhug.example.presentation.view.activity

import android.view.View
import com.dhug.example.base.AppAdsActivity
import com.dhug.example.databinding.ActivityHomeBinding

class HomeActivity: AppAdsActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
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