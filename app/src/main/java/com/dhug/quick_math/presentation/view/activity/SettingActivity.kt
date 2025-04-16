package com.dhug.quick_math.presentation.view.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhug.quick_math.R
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.data.local.entities.Setting
import com.dhug.quick_math.databinding.ActivitySettingBinding
import com.dhug.quick_math.presentation.viewmodel.SettingViewModel
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.AppUtils
import com.dhug.quick_math.utils.LanguageConstants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingActivity : AppAdsActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val settingViewModel: SettingViewModel by viewModels()
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        setOnClickListener(
            binding.btnBack,
            binding.btnLanguage,
            binding.btnPrivacyPolicy,
            binding.btnReviewApp,
            binding.btnShareApp,
        )
    }

    override fun observerData() {
        super.observerData()
        lifecycleScope.launch {
            languageViewModel.languageFlow.collectLatest {
                updateFlagView(it)
            }
        }

        lifecycleScope.launch {
            settingViewModel.settingCurrent.collectLatest {
                updateCurrentSetting(it)
            }
        }
    }

    private fun updateCurrentSetting(setting: Setting) {
        binding.btnEnable.isChecked = setting.enableSaveTraining
        binding.edtSecond.setText(setting.secondTime.toString())
    }

    private fun updateFlagView(code: String) {
        when (code) {
            LanguageConstants.US -> binding.imvFlag.setImageResource(R.drawable.ic_flag_usa)
            else -> binding.imvFlag.setImageResource(R.drawable.ic_flag_vn)
        }
    }

    override fun initData() {
        //
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    override fun onClickNormal(view: View) {
        super.onClickNormal(view)
        when (view) {
            binding.btnBack -> {
                finish()
            }

            binding.btnLanguage -> {
                updateFlag()
            }

            binding.btnPrivacyPolicy -> {
                AppUtils.openBrowser(AppConstants.URL_POLICY, this)
            }

            binding.btnReviewApp -> {
                AppUtils.rateMyApp(this)
            }

            binding.btnShareApp -> {
                AppUtils.shareMyApp(this)
            }
        }
    }

    private fun updateFlag() {
        if (languageViewModel.languageFlow.value == LanguageConstants.US) {
            languageViewModel.changeLanguage(LanguageConstants.VN)
        } else {
            languageViewModel.changeLanguage(LanguageConstants.US)
        }
    }
}