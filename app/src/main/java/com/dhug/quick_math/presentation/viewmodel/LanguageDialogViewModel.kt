package com.dhug.quick_math.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dhug.quick_math.R
import com.dhug.quick_math.data.local.entities.Language
import com.dhug.quick_math.utils.LanguageConstants
import com.dhug.quick_math.utils.MMKVUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageDialogViewModel.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 19/4/25 at 09:36
 * Description: File LanguageDialogViewModel.kt created by admin - 19/4/25 at 09:36
 */
@HiltViewModel
class LanguageDialogViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _languages: MutableStateFlow<List<Language>> = MutableStateFlow(initLanguages())

    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    private fun initLanguages(): List<Language> {
        return listOf(
            Language(
                language = context.getString(R.string.vietnamese),
                code = LanguageConstants.VN,
                logo = R.drawable.ic_flag_vn,
                isChecked = isSelectedLanguage(
                    LanguageConstants.VN
                )
            ),
            Language(
                language = context.getString(R.string.usa),
                code = LanguageConstants.US,
                logo = R.drawable.ic_flag_usa,
                isChecked = isSelectedLanguage(
                    LanguageConstants.US
                )
            ),
        )
    }

    private fun isSelectedLanguage(code: String): Boolean = code == MMKVUtils.languageFlow.value

    fun getCurrentPosition() = _languages.value.indexOfFirst { it.isChecked }
}