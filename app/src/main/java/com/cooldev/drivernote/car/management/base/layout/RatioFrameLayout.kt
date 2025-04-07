package com.dhug.example.base.layout

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.FrameLayout
import com.dhug.example.R


/**
* author: Android Wheel Brother
 * github: https://github.com/getActivity/AndroidProject-Kotlin
 * time: 2019/08/23
 * desc: FrameLayout displayed proportionally
 */
class RatioFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

   /** Width to height ratio */
    private var widthRatio: Float = 0f
    private var heightRatio: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var finalWidthMeasureSpec: Int = widthMeasureSpec
        var finalHeightMeasureSpec: Int = heightMeasureSpec
        if (widthRatio != 0f && heightRatio != 0f) {
            val sizeRatio: Float = getSizeRatio()
            val widthSpecMode: Int = MeasureSpec.getMode(finalWidthMeasureSpec)
            val widthSpecSize: Int = MeasureSpec.getSize(finalWidthMeasureSpec)
            val heightSpecMode: Int = MeasureSpec.getMode(finalHeightMeasureSpec)
            val heightSpecSize: Int = MeasureSpec.getSize(finalHeightMeasureSpec)

            // Generally, LayoutParams.WRAP_CONTENT corresponds to MeasureSpec.AT_MOST (adaptive), but because we forcibly modified the measurement mode in the code to MeasureSpec.EXACTLY (fixed value)
            // In this way, it is possible to retrigger the onMeasure method. At this time, the measurement mode passed in is not the MeasureSpec.AT_MOST (adaptive) mode, but the MeasureSpec.EXACTLY (fixed value) mode.
            // So we need to make a double judgment, first judge the LayoutParams, and then judge the measurement mode. This can avoid triggering a recalculation of the width and height due to modification of the measurement mode, which ultimately leads to the calculation result being different from the last calculation.
            if ((layoutParams.width != LayoutParams.WRAP_CONTENT) && (layoutParams.height != LayoutParams.WRAP_CONTENT) &&
                (widthSpecMode == MeasureSpec.EXACTLY) && (heightSpecMode == MeasureSpec.EXACTLY)) {
                // If the current width and height are hard-coded
                if (widthSpecSize / sizeRatio <= heightSpecSize) {
                    // If the width does not exceed the original height after proportional conversion
                    finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (widthSpecSize / sizeRatio).toInt(),
                        MeasureSpec.EXACTLY
                    )
                } else if (heightSpecSize * sizeRatio <= widthSpecSize) {
                    // If the height does not exceed the original width after proportional conversion
                    finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (heightSpecSize * sizeRatio).toInt(),
                        MeasureSpec.EXACTLY
                    )
                }
            } else if ((layoutParams.width != LayoutParams.WRAP_CONTENT) && (widthSpecMode == MeasureSpec.EXACTLY) && (heightSpecMode != MeasureSpec.EXACTLY)) {
                // If the current width is hard-coded, but the height is not hard-coded
                finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (widthSpecSize / sizeRatio).toInt(),
                    MeasureSpec.EXACTLY
                )
            } else if ((layoutParams.height != LayoutParams.WRAP_CONTENT) && (heightSpecMode == MeasureSpec.EXACTLY) && (widthSpecMode != MeasureSpec.EXACTLY)) {
                // If the current height is hard-coded, but the width is not hard-coded
                finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (heightSpecSize * sizeRatio).toInt(),
                    MeasureSpec.EXACTLY
                )
            }
        }
        super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec)
    }

    fun getWidthRatio(): Float {
        return widthRatio
    }

    fun getHeightRatio(): Float {
        return heightRatio
    }

    /**
     * Get aspect ratio
     */
    fun getSizeRatio(): Float {
        return widthRatio / heightRatio
    }

    /**
     * Set aspect ratio
     */
    fun setSizeRatio(widthRatio: Float, heightRatio: Float) {
        this.widthRatio = widthRatio
        this.heightRatio = heightRatio
        requestLayout()
        invalidate()
    }

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout)
        val sizeRatio: String? = array.getString(R.styleable.RatioFrameLayout_sizeRatio)
        if (!TextUtils.isEmpty(sizeRatio)) {
            val arrays: Array<String> = sizeRatio!!.split(":").toTypedArray()
            when (arrays.size) {
                1 -> {
                    widthRatio = arrays[0].toFloat()
                    heightRatio = 1f
                }
                2 -> {
                    widthRatio = arrays[0].toFloat()
                    heightRatio = arrays[1].toFloat()
                }
                else -> throw IllegalArgumentException("are you ok?")
            }
        }
        array.recycle()
    }
}