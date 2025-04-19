package com.dhug.wiget.wiget

import android.R
import android.content.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class SmartTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.textViewStyle) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        refreshVisibilityStatus()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        refreshVisibilityStatus()
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawables(left, top, right, bottom)
        refreshVisibilityStatus()
    }

    override fun setCompoundDrawablesRelative(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawablesRelative(start, top, end, bottom)
        refreshVisibilityStatus()
    }

   /**
     * Refresh the current visible state
     */
    private fun refreshVisibilityStatus() {
        // Determine whether there is currently set text to achieve the effect of automatically hiding and displaying
        if (isEmptyContent() && visibility != GONE) {
            visibility = GONE
            return
        }
        if (visibility != VISIBLE) {
            visibility = VISIBLE
        }
    }

    /**
     * Whether the TextView content is empty
     */
    private fun isEmptyContent(): Boolean {
        if (!TextUtils.isEmpty(text)) {
            return false
        }
        val compoundDrawables: Array<Drawable?> = compoundDrawables
        val compoundDrawablesRelative: Array<Drawable?> = compoundDrawablesRelative
        for (drawable: Drawable? in compoundDrawables) {
            if (drawable != null) {
                return false
            }
        }
        for (drawable: Drawable? in compoundDrawablesRelative) {
            if (drawable != null) {
                return false
            }
        }
        return true
    }
}