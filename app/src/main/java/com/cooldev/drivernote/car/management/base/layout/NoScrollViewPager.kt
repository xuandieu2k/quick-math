package com.dhug.example.base.layout

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * author: Android Wheel Brother
 * github: https://github.com/getActivity/AndroidProject-Kotlin
 * time: 2018/10/18
 * desc: disable horizontal sliding ViewPager (generally used for ViewPager + Fragment on the APP homepage)
 */
class NoScrollViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //Do not intercept this event
        return false
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        //Do not handle this event
        return false
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        //Do not respond to key events
        return false
    }

    override fun setCurrentItem(item: Int) {
        // Only adjacent pages will have animations
        super.setCurrentItem(item, abs(currentItem - item) == 1)
    }
}