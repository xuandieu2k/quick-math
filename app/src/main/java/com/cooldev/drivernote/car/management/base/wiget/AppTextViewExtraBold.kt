package com.dhug.example.base.wiget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.cooldev.base.R

class AppTextViewExtraBold  : AppCompatTextView {
    private var typeFont = ResourcesCompat.getFont(context, R.font.plus_jakarta_sans_extra_bold)

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