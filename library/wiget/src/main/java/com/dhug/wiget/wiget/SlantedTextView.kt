package com.dhug.wiget.wiget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.annotation.StringRes
import com.dhug.wiget.R
import kotlin.math.max

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("RtlHardcoded")
class SlantedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {

        /** Rotation angle */
        const val ROTATE_ANGLE: Int = 45
    }

    /** Background brush */
    private val backgroundPaint: Paint = Paint()

    /** Text brush */
    private val textPaint: TextPaint

    /** Displayed text */
    private var text: String = ""

    /** Tilt center of gravity */
    private var gravity: Int = 0

    /** Whether to draw a triangle */
    private var triangle: Boolean = false

    /** background color */
    private var colorBackground: Int = 0

    /** Text measurement range loading */
    private val textBounds: Rect = Rect()

    /** Measured text height */
    private var textHeight: Int = 0

    init {
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        backgroundPaint.isAntiAlias = true
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.isAntiAlias = true
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SlantedTextView)
        setText(array.getString(R.styleable.SlantedTextView_android_text))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimensionPixelSize(
            R.styleable.SlantedTextView_android_textSize,
            resources.getDimension(com.dhug.base.R.dimen.sp_12).toInt()).toFloat())
        setTextColor(array.getColor(R.styleable.SlantedTextView_android_textColor, Color.WHITE))
        setTextStyle(Typeface.defaultFromStyle(array.getInt(R.styleable.SlantedTextView_android_textStyle, Typeface.NORMAL)))
        setGravity(array.getInt(R.styleable.SlantedTextView_android_gravity, Gravity.END))
        setColorBackground(array.getColor(R.styleable.SlantedTextView_android_colorBackground, getAccentColor()))
        setTriangle(array.getBoolean(R.styleable.SlantedTextView_triangle, false))
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textHeight = textBounds.height() + paddingTop + paddingBottom
        var width = 0
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> width = MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> width =
                textBounds.width() + paddingLeft + paddingRight
        }
        var height = 0
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                height = MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                height = textBounds.height() + paddingTop + paddingBottom
            }
        }
        setMeasuredDimension(max(width, height), max(width, height))
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawText(canvas)
    }

    /**
     * Draw background
     */
    private fun drawBackground(canvas: Canvas) {
        val path = Path()
        val width: Int = canvas.width
        val height: Int = canvas.height
        when (gravity) {
            Gravity.LEFT, Gravity.LEFT or Gravity.TOP -> {
                if (triangle) {
                    path.lineTo(0f, height.toFloat())
                    path.lineTo(width.toFloat(), 0f)
                } else {
                    path.moveTo(width.toFloat(), 0f)
                    path.lineTo(0f, height.toFloat())
                    path.lineTo(0f, (height - textHeight).toFloat())
                    path.lineTo((width - textHeight).toFloat(), 0f)
                }
            }
            Gravity.NO_GRAVITY, Gravity.RIGHT, Gravity.RIGHT or Gravity.TOP -> {
                if (triangle) {
                    path.lineTo(width.toFloat(), 0f)
                    path.lineTo(width.toFloat(), height.toFloat())
                } else {
                    path.lineTo(width.toFloat(), height.toFloat())
                    path.lineTo(width.toFloat(), (height - textHeight).toFloat())
                    path.lineTo(textHeight * 1f, 0f)
                }
            }
            Gravity.BOTTOM, Gravity.LEFT or Gravity.BOTTOM -> {
                if (triangle) {
                    path.lineTo(width.toFloat(), height.toFloat())
                    path.lineTo(0f, height.toFloat())
                } else {
                    path.lineTo(width.toFloat(), height.toFloat())
                    path.lineTo((width - textHeight).toFloat(), height.toFloat())
                    path.lineTo(0f, textHeight.toFloat())
                }
            }
            Gravity.RIGHT or Gravity.BOTTOM -> {
                if (triangle) {
                    path.moveTo(0f, height.toFloat())
                    path.lineTo(width.toFloat(), height.toFloat())
                    path.lineTo(width.toFloat(), 0f)
                } else {
                    path.moveTo(0f, height.toFloat())
                    path.lineTo(textHeight * 1f, height.toFloat())
                    path.lineTo(width.toFloat(), textHeight.toFloat())
                    path.lineTo(width.toFloat(), 0f)
                }
            }
            else -> {
                throw IllegalArgumentException("are you ok?")
            }
        }
        path.close()
        canvas.drawPath(path, backgroundPaint)
        canvas.save()
    }

    /**
     * Draw text
     */
    private fun drawText(canvas: Canvas) {
        val width: Int = canvas.width - textHeight / 2
        val height: Int = canvas.height - textHeight / 2
        val rect: Rect?
        val rectF: RectF?
        val offset: Int = textHeight / 2
        val toX: Float
        val toY: Float
        val centerX: Float
        val centerY: Float
        val angle: Float
        when (gravity) {
            Gravity.LEFT, Gravity.LEFT or Gravity.TOP -> {
                rect = Rect(0, 0, width, height)
                rectF = RectF(rect)
                rectF.right = textPaint.measureText(text, 0, text.length)
                rectF.bottom = textPaint.descent() - textPaint.ascent()
                rectF.left += (rect.width() - rectF.right) / 2.0f
                rectF.top += (rect.height() - rectF.bottom) / 2.0f
                toX = rectF.left
                toY = rectF.top - textPaint.ascent()
                centerX = width / 2f
                centerY = height / 2f
                angle = -ROTATE_ANGLE.toFloat()
            }
            Gravity.NO_GRAVITY, Gravity.RIGHT, Gravity.RIGHT or Gravity.TOP -> {
                rect = Rect(offset, 0, width + offset, height)
                rectF = RectF(rect)
                rectF.right = textPaint.measureText(text, 0, text.length)
                rectF.bottom = textPaint.descent() - textPaint.ascent()
                rectF.left += (rect.width() - rectF.right) / 2.0f
                rectF.top += (rect.height() - rectF.bottom) / 2.0f
                toX = rectF.left
                toY = rectF.top - textPaint.ascent()
                centerX = width / 2f + offset
                centerY = height / 2f
                angle = ROTATE_ANGLE.toFloat()
            }
            Gravity.BOTTOM, Gravity.LEFT or Gravity.BOTTOM -> {
                rect = Rect(0, offset, width, height + offset)
                rectF = RectF(rect)
                rectF.right = textPaint.measureText(text, 0, text.length)
                rectF.bottom = textPaint.descent() - textPaint.ascent()
                rectF.left += (rect.width() - rectF.right) / 2.0f
                rectF.top += (rect.height() - rectF.bottom) / 2.0f
                toX = rectF.left
                toY = rectF.top - textPaint.ascent()
                centerX = width / 2f
                centerY = height / 2f + offset
                angle = ROTATE_ANGLE.toFloat()
            }
            Gravity.RIGHT or Gravity.BOTTOM -> {
                rect = Rect(offset, offset, width + offset, height + offset)
                rectF = RectF(rect)
                rectF.right = textPaint.measureText(text, 0, text.length)
                rectF.bottom = textPaint.descent() - textPaint.ascent()
                rectF.left += (rect.width() - rectF.right) / 2.0f
                rectF.top += (rect.height() - rectF.bottom) / 2.0f
                toX = rectF.left
                toY = rectF.top - textPaint.ascent()
                centerX = width / 2f + offset
                centerY = height / 2f + offset
                angle = -ROTATE_ANGLE.toFloat()
            }
            else -> {
                throw IllegalArgumentException("are you ok?")
            }
        }
        canvas.rotate(angle, centerX, centerY)
        canvas.drawText(text, toX, toY, textPaint)
    }

    /**
     * Get the display text
     */
    fun getText(): String {
        return text
    }

    /**
     * Set display text
     */
    fun setText(@StringRes id: Int) {
        setText(resources.getString(id))
    }

    fun setText(text: String?) {
        val finalText = text ?: ""
        if (!TextUtils.equals(finalText, getText())) {
            this.text = finalText
            invalidate()
        }
    }

    /**
     * Get font color
     */
    fun getTextColor(): Int {
        return textPaint.color
    }

    /**
     * Set font color
     */
    fun setTextColor(color: Int) {
        if (getTextColor() != color) {
            textPaint.color = color
            invalidate()
        }
    }

    /**
     * Get font size
     */
    fun getTextSize(): Float {
        return textPaint.textSize
    }

    /**
     * Set font size
     */
    fun setTextSize(size: Float) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setTextSize(unit: Int, size: Float) {
        val textSize: Float = TypedValue.applyDimension(unit, size, resources.displayMetrics)
        if (getTextSize() != textSize) {
            textPaint.textSize = textSize
            invalidate()
        }
    }

    /**
     * Get text style
     */
    fun getTextStyle(): Typeface? {
        return textPaint.typeface
    }

    /**
     * Set text style
     */
    fun setTextStyle(tf: Typeface?) {
        if (getTextStyle() !== tf) {
            textPaint.typeface = tf
            invalidate()
        }
    }

    /**
     * Get the background color
     */
    fun getColorBackground(): Int {
        return colorBackground
    }

    /**
     * Set background color
     */
    fun setColorBackground(color: Int) {
        if (getColorBackground() != color) {
            colorBackground = color
            backgroundPaint.color = colorBackground
            invalidate()
        }
    }

    /**
     * Get the tilt center of gravity
     */
    fun getGravity(): Int {
        return gravity
    }

    /**
     * Set the tilt center of gravity
     */
    fun setGravity(gravity: Int) {
        if (this.gravity != gravity) {
            // Adapt layout in reverse direction
            this.gravity = Gravity.getAbsoluteGravity(gravity, resources.configuration.layoutDirection)
            invalidate()
        }
    }

    /**
     * Whether it is currently a triangle
     */
    fun isTriangle(): Boolean {
        return triangle
    }

    /**
     * Whether to set it to a triangle
     */
    fun setTriangle(triangle: Boolean) {
        if (isTriangle() != triangle) {
            this.triangle = triangle
            invalidate()
        }
    }

    /**
     * Get the accent color of the current theme
     */
    private fun getAccentColor(): Int {
        val typedValue = TypedValue()
//        context.theme.resolveAttribute(com.hjq.shape.R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }
}