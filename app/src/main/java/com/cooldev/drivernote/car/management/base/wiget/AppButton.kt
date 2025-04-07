package com.dhug.example.base.wiget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import com.cooldev.base.R

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 01 / 05 / 2024
 */
class AppButton : AppCompatButton {
    private var typeFont = ResourcesCompat.getFont(context, R.font.plus_fakarta_sans_regular)

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