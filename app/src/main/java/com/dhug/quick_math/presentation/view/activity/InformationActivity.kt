package com.dhug.quick_math.presentation.view.activity

import android.view.View
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.databinding.ActivityInformationBinding

class InformationActivity : AppAdsActivity() {
    private lateinit var binding: ActivityInformationBinding
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivityInformationBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun initView() {
        setOnClickListener(binding.btnBack)
    }

    override fun initData() {
        //
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM
}