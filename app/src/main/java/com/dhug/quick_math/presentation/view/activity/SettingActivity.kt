package com.dhug.quick_math.presentation.view.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhug.quick_math.R
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.base.wiget.AppToast
import com.dhug.quick_math.data.local.entities.Language
import com.dhug.quick_math.data.local.entities.Setting
import com.dhug.quick_math.databinding.ActivitySettingBinding
import com.dhug.quick_math.presentation.dialog.LanguageDialog
import com.dhug.quick_math.presentation.viewmodel.SettingViewModel
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.AppUtils
import com.dhug.quick_math.utils.LanguageConstants
import com.dhug.quick_math.utils.NumberUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SettingActivity : AppAdsActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val settingViewModel: SettingViewModel by viewModels()


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            //
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            //
        }

        override fun afterTextChanged(s: Editable?) {
            binding.edtSecond.removeTextChangedListener(this)
            val data = NumberUtils.textToBigDecimal(s.toString())
            if (data in (BigDecimal.valueOf(5)..BigDecimal.valueOf(
                    100
                ))
            ) {
                settingViewModel.updateSetting(data.toInt())
                binding.edtSecond.setSelection(binding.edtSecond.text.toString().length)
                binding.edtSecond.addTextChangedListener(this)
                return
            }

            if (data < BigDecimal.valueOf(5)) {
                binding.edtSecond.setText(5.toString())
                binding.edtSecond.setSelection(binding.edtSecond.text.toString().length)
                AppToast(
                    this@SettingActivity,
                    getString(R.string.number_range_invalid),
                    Toast.LENGTH_SHORT
                ).show()
                settingViewModel.updateSetting(5)
                binding.edtSecond.addTextChangedListener(this)
                return
            }

            if (data >= BigDecimal.valueOf(120)) {
                binding.edtSecond.setText(120.toString())
                binding.edtSecond.setSelection(binding.edtSecond.text.toString().length)
                AppToast(
                    this@SettingActivity,
                    getString(R.string.number_range_invalid),
                    Toast.LENGTH_SHORT
                ).show()
                settingViewModel.updateSetting(120)
                binding.edtSecond.addTextChangedListener(this)
                return
            }
        }
    }

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
        binding.edtSecond.removeTextChangedListener(textWatcher)
        binding.edtSecond.setText(setting.secondTime.toString())
        binding.edtSecond.setSelection(setting.secondTime.toString().length)
        binding.edtSecond.addTextChangedListener(textWatcher)
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

    override fun onDestroy() {
        super.onDestroy()
        binding.edtSecond.removeTextChangedListener(textWatcher)
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    private fun showDialogLanguage(onDone: (isConfirm: Boolean, language: Language) -> Unit) {
        LanguageDialog.Builder(this, this)
            .setListenerAction(object : LanguageDialog.Builder.OnActionLanguage {
                override fun onChooseLanguage(isConfirm: Boolean, language: Language) {
                    onDone(isConfirm, language)
                }

            }).show()
    }

    override fun onClickNormal(view: View) {
        super.onClickNormal(view)
        when (view) {
            binding.btnBack -> {
                finish()
            }

            binding.btnLanguage -> {
                showDialogLanguage { isConfirm, language ->
                    if (isConfirm) {
                        updateLanguage(language.code)
                    }
                }
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

    private fun updateLanguage(code: String) {
        languageViewModel.changeLanguage(code)
    }
}