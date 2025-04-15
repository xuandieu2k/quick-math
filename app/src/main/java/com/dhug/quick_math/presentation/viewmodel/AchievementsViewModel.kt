package com.dhug.quick_math.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.dhug.quick_math.data.local.entities.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    AchievementsViewModel.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 10:33
 * Description: File AchievementsViewModel.kt created by admin - 12/4/25 at 10:33
 */

@HiltViewModel
class AchievementsViewModel @Inject constructor() : ViewModel() {

    private val _scores: MutableStateFlow<PagingData<Score>?> = MutableStateFlow(null)
    val scores: StateFlow<PagingData<Score>?> = _scores.asStateFlow()
}