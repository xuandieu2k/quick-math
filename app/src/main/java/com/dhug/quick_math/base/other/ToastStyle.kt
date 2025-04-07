package com.dhug.quick_math.base.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.dhug.quick_math.R
import com.hjq.toast.style.BlackToastStyle

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2021/02/27
 *    desc   : Toast 样式配置
 */
class ToastStyle : BlackToastStyle() {

    override fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        //Set color
        drawable.setColor(-0x78000000)
        //Set rounded corners
        drawable.cornerRadius = context.resources.getDimension(R.dimen.button_circle_size)
        return drawable
    }

    override fun getTextSize(context: Context): Float {
        return context.resources.getDimension(com.cooldev.base.R.dimen.sp_14)
    }

    override fun getHorizontalPadding(context: Context): Int {
        return context.resources.getDimension(com.cooldev.base.R.dimen.sp_24).toInt()
    }

    override fun getVerticalPadding(context: Context): Int {
        return context.resources.getDimension(com.cooldev.base.R.dimen.sp_16).toInt()
    }
}