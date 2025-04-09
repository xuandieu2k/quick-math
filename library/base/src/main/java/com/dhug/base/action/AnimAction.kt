package com.dhug.base.action

import com.cooldev.base.R

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
interface AnimAction {

    companion object {

        /**Default animation effect */
        const val ANIM_DEFAULT: Int = -1

        /** No animation effect */
        const val ANIM_EMPTY: Int = 0

        /** Zoom animation */
        val ANIM_SCALE: Int = R.style.ScaleAnimStyle

        /** IOS animation */
        val ANIM_IOS: Int = R.style.IOSAnimStyle

        /** Toast animation */
        const val ANIM_TOAST: Int = android.R.style.Animation_Toast

        /** Top pop-up animation */
        val ANIM_TOP: Int = R.style.TopAnimStyle

        /** Bottom pop-up animation */
        val ANIM_BOTTOM: Int = R.style.BottomAnimStyle

        /** Pop-up animation on the left */
        val ANIM_LEFT: Int = R.style.LeftAnimStyle

        /** Pop-up animation on the right */
        val ANIM_RIGHT: Int = R.style.RightAnimStyle
    }
}