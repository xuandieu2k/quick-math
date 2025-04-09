package com.dhug.quick_math.presentation.view.activity

import android.content.Intent
import android.view.View
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppAdsActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        setOnClickListener(
            binding.btnPlay,
            binding.btnTraining,
            binding.btnAchievements,
            binding.btnSetting,
            binding.btnInformation,
        )
    }

    override fun initData() {
        //
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    override fun onClickNormal(view: View) {
        when (view) {
            binding.btnPlay -> {
                startActivity(Intent(this, MatchActivity::class.java))
            }
            binding.btnTraining ->{
                startActivity(Intent(this, TrainingActivity::class.java))
            }
            binding.btnAchievements ->{

            }
            binding.btnSetting ->{
                startActivity(Intent(this, SettingActivity::class.java))
            }
            binding.btnInformation ->{
                startActivity(Intent(this, InformationActivity::class.java))
            }
        }
    }
}