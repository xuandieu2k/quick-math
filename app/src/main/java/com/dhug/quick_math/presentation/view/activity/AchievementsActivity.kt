package com.dhug.quick_math.presentation.view.activity

import android.view.View
import com.dhug.base.PagerAdapter
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.databinding.ActivityAchievementsBinding
import com.dhug.quick_math.presentation.view.fragment.CompetitionFragment
import com.dhug.quick_math.presentation.view.fragment.TrainingFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    AchievementsActivity.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 10:33
 * Description: File AchievementsActivity.kt created by admin - 12/4/25 at 10:33
 */

@AndroidEntryPoint
class AchievementsActivity : AppAdsActivity() {
    private val binding: ActivityAchievementsBinding by lazy {
        ActivityAchievementsBinding.inflate(
            layoutInflater
        )
    }
    private val adapter = PagerAdapter(this, listOf(CompetitionFragment(), TrainingFragment()))
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View = binding.root
    override fun initView() {
        setUpView()
        setOnClickListener(
            binding.btnCompetition,
            binding.btnTraining,
            binding.btnBack,
        )
    }

    private fun setUpView() {
        binding.vpTab.adapter = adapter
        binding.vpTab.isUserInputEnabled = false
        binding.vpTab.currentItem = 0
        binding.btnCompetition.isSelected = true
        binding.vpTab.offscreenPageLimit = 2
    }

    override fun initData() {
        //
    }

    private fun switchFragment(value: Int) {
        binding.btnCompetition.isSelected = value == 0
        binding.btnTraining.isSelected = value != 0
        binding.vpTab.currentItem = value
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    override fun onClickNormal(view: View) {
        super.onClickNormal(view)
        when (view) {
            binding.btnCompetition -> {
                switchFragment(0)
            }

            binding.btnTraining -> {
                switchFragment(1)
            }

            binding.btnBack -> {
                finish()
            }
        }
    }
}