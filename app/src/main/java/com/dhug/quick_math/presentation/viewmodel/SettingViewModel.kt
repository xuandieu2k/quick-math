package com.dhug.quick_math.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.dhug.quick_math.utils.MMKVUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    SettingViewModel.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 16/4/25 at 21:38
 * Description: File SettingViewModel.kt created by admin - 16/4/25 at 21:38
 */
@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {
    val settingCurrent = MMKVUtils.settingFlow
}