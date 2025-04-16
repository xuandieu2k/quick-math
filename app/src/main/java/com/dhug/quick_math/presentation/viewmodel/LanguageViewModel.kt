package com.dhug.quick_math.presentation.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.quick_math.domain.usecase.LanguageUseCase
import com.dhug.quick_math.utils.MMKVUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageViewModel.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 15/4/25 at 20:57
 * Description: File LanguageViewModel.kt created by admin - 15/4/25 at 20:57
 */
@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageUseCase: LanguageUseCase,
) : ViewModel() {

    val languageFlow = MMKVUtils.languageFlow

    fun changeLanguage(code: String) {
        viewModelScope.launch {
            languageUseCase.saveLanguage(code)
            val locale = LocaleListCompat.forLanguageTags(code)
            AppCompatDelegate.setApplicationLocales(locale)
        }
    }
}
