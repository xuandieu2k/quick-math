package com.dhug.quick_math.base.wiget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.dhug.base.R

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 01 / 05 / 2024
 */
open class AppTextView : AppCompatTextView {
    private var typeFont = ResourcesCompat.getFont(context, R.font.goldman_regular)

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        typeface = typeFont
    }
}