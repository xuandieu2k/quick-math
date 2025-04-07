package com.dhug.quick_math.base.action

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
interface TitleBarAction : OnTitleBarListener {

    /**
     * Get the title bar object
     */
    fun getTitleBar(): TitleBar?

    /**
     * The left item is clicked
     *
     * @param view the clicked left item View
     */
    override fun onLeftClick(view: View) {}

    /**
     *The title is clicked
     *
     * @param view the clicked title View
     */
    override fun onTitleClick(view: View) {}

    /**
     * The right item is clicked
     *
     * @param view the clicked right item View
     */
    override fun onRightClick(view: View) {}

    /**
     * Set the title of the title bar
     */
    fun setTitle(@StringRes id: Int) {
        getTitleBar()?.setTitle(id)
    }

    /**
     * Set the title of the title bar
     */
    fun setTitle(title: CharSequence?) {
        getTitleBar()?.title = title
    }

    /**
     * Set the left title of the title bar
     */
    fun setLeftTitle(id: Int) {
        getTitleBar()?.setLeftTitle(id)
    }

    fun setLeftTitle(text: CharSequence?) {
        getTitleBar()?.leftTitle = text
    }

    fun getLeftTitle(): CharSequence? {
        return getTitleBar()?.leftTitle
    }

    /**
     * Set the right title of the title bar
     */
    fun setRightTitle(id: Int) {
        getTitleBar()?.setRightTitle(id)
    }

    fun setRightTitle(text: CharSequence?) {
        getTitleBar()?.rightTitle = text
    }

    fun getRightTitle(): CharSequence? {
        return getTitleBar()?.rightTitle
    }

    /**
     * Set the left icon of the title bar
     */
    fun setLeftIcon(id: Int) {
        getTitleBar()?.setLeftIcon(id)
    }

    fun setLeftIcon(drawable: Drawable?) {
        getTitleBar()?.leftIcon = drawable
    }

    fun getLeftIcon(): Drawable? {
        return getTitleBar()?.leftIcon
    }

    /**
     * Set the right icon of the title bar
     */
    fun setRightIcon(id: Int) {
        getTitleBar()?.setRightIcon(id)
    }

    fun setRightIcon(drawable: Drawable?) {
        getTitleBar()?.rightIcon = drawable
    }

    fun getRightIcon(): Drawable? {
        return getTitleBar()?.rightIcon
    }

    /**
     * Recursively obtain the TitleBar object in ViewGroup
     */
    fun obtainTitleBar(group: ViewGroup?): TitleBar? {
        if (group == null) {
            return null
        }
        for (i in 0 until group.childCount) {
            val view = group.getChildAt(i)
            if (view is TitleBar) {
                return view
            }
            if (view is ViewGroup) {
                val titleBar = obtainTitleBar(view)
                if (titleBar != null) {
                    return titleBar
                }
            }
        }
        return null
    }
}