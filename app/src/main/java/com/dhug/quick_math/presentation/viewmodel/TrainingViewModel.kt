package com.dhug.quick_math.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dhug.quick_math.domain.usecase.ScoreUseCase
import com.dhug.quick_math.utils.EnumConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    TrainingViewModel.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 11:01
 * Description: File TrainingViewModel.kt created by admin - 12/4/25 at 11:01
 */

@HiltViewModel
class TrainingViewModel @Inject constructor(private val scoreUseCase: ScoreUseCase) : ViewModel() {

    val scores =
        scoreUseCase.paging(10, EnumConstants.PlayType.TRAINING).cachedIn(viewModelScope)
            .stateIn(
                viewModelScope, SharingStarted.Lazily, null
            )
}