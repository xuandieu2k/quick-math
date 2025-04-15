package com.dhug.quick_math.presentation.view.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.dhug.quick_math.base.AppFullFragment
import com.dhug.quick_math.databinding.FragmentTrainingBinding
import com.dhug.quick_math.presentation.adapter.HistoryAdapter
import com.dhug.quick_math.presentation.viewmodel.TrainingViewModel
import com.dhug.quick_math.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    TrainingFragment.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 10:50
 * Description: File TrainingFragment.kt created by admin - 12/4/25 at 10:50
 */

@AndroidEntryPoint
class TrainingFragment : AppFullFragment() {
    private lateinit var binding: FragmentTrainingBinding

    @Inject
    lateinit var historyAdapter: HistoryAdapter

    private val competitionViewModel: TrainingViewModel by activityViewModels()
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View? {
        binding = FragmentTrainingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        binding.rvHistory.adapter = historyAdapter
        historyAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                val snapshot = historyAdapter.snapshot().items
                Timber.tag("Log Size:").d(snapshot.size.toString())
            }

            val isEmpty = loadState.refresh is LoadState.NotLoading &&
                    historyAdapter.itemCount == 0
            setUpViewEmpty(isEmpty)
        }
        AppUtils.initRecyclerView(binding.rvHistory, historyAdapter, AppUtils.Orientation.VERTICAL)
        setUpViewEmpty(isShowEmpty = true)
    }

    private fun setUpViewEmpty(isShowEmpty: Boolean) {
        binding.rvHistory.isVisible = !isShowEmpty
        binding.layoutEmpty.root.isVisible = isShowEmpty
    }

    override fun initData() {
        //
    }

    override fun observerData() {
        lifecycleScope.launch {
            competitionViewModel.scores.collectLatest {
                it?.let { historyAdapter.submitData(it) }
            }

        }
    }
}