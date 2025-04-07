package com.dhug.quick_math.utils

import android.view.View
import javax.inject.Singleton

@Singleton
object AnimationUtils {
    fun View.toggleDropList() {
        if (this.visibility == View.VISIBLE) {
            this.animate()
                .alpha(0f)
                .translationY(-this.height.toFloat())
                .setDuration(300)
                .withEndAction { this.visibility = View.GONE }
                .start()
        } else {
            this.visibility = View.VISIBLE
            this.alpha = 0f
            this.translationY = -this.height.toFloat()
            this.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    fun View.toggleDropList(isShow: Boolean) {
        if (isShow) {
            this.animate()
                .alpha(0f)
                .translationY(-this.height.toFloat())
                .setDuration(300)
                .withEndAction { this.visibility = View.GONE }
                .start()
        } else {
            this.visibility = View.VISIBLE
            this.alpha = 0f
            this.translationY = -this.height.toFloat()
            this.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

}