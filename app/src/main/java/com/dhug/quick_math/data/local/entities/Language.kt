package com.dhug.quick_math.data.local.entities

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.dhug.quick_math.utils.LanguageConstants

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    Language.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 19/4/25 at 09:21
 * Description: File Language.kt created by admin - 19/4/25 at 09:21
 */
data class Language(
    var id: Long = 0L,
    var language: String = "",
    var code: String = LanguageConstants.VN,
    var isChecked: Boolean = false,
    var logo: Int = 0
) {
    fun getDrawableWithLanguage(context: Context): Drawable? =
        AppCompatResources.getDrawable(context, logo)

    fun isVisibleWithChecked(): Int = if (isChecked) View.VISIBLE else View.GONE
}