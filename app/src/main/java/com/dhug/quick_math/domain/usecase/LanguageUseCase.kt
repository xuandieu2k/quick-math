package com.dhug.quick_math.domain.usecase

import com.dhug.quick_math.utils.MMKVUtils
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageUseCase.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 15/4/25 at 20:51
 * Description: File LanguageUseCase.kt created by admin - 15/4/25 at 20:51
 */

class LanguageUseCase @Inject constructor() {

    fun saveLanguage(code: String) {
        MMKVUtils.setLanguage(code)
    }

    fun getCurrentLanguage(): String = MMKVUtils.getLanguage()
}