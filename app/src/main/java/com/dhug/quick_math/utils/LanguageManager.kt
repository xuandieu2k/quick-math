package com.dhug.quick_math.utils

import android.app.Activity
import android.content.Context
import java.util.Locale

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageManager.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 15/4/25 at 20:48
 * Description: File LanguageManager.kt created by admin - 15/4/25 at 20:48
 */
object LanguageManager {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun applyLanguage(context: Context, languageCode: String) {
        val newContext = setLocale(context, languageCode)
        if (context is Activity) {
            context.recreate()
        }
    }
}
