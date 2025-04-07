package com.dhug.example.base.layout

import android.content.*
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.dhug.example.R


/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class CustomViewStub @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var listener: OnViewStubListener? = null
    private val layoutResource: Int
    private var inflateView: View? = null

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomViewStub)
        layoutResource = array.getResourceId(R.styleable.CustomViewStub_android_layout, 0)
        array.recycle()

        // HIDE YOURSELF
        visibility = GONE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (inflateView == null && visibility != GONE) {
            inflateView = LayoutInflater.from(context).inflate(layoutResource, this, false)
            val layoutParams: LayoutParams? = inflateView!!.layoutParams as LayoutParams?
            if (layoutParams != null) {
                layoutParams.width = getLayoutParams().width
                layoutParams.height = getLayoutParams().height
                if (layoutParams.gravity == LayoutParams.UNSPECIFIED_GRAVITY) {
                    layoutParams.gravity = Gravity.CENTER
                }
                inflateView!!.layoutParams = layoutParams
            }
            addView(inflateView)
            listener?.onInflate(this, inflateView!!)
        }
        listener?.onVisibility(this, visibility)
    }

    /**
     * Set display status (avoid infinite recursion caused by setVisibility)
     */
    fun setCustomVisibility(visibility: Int) {
        super.setVisibility(visibility)
    }

    /**
     * Get the populated View
     */
    fun getInflateView(): View? {
        return inflateView
    }

    /**
     * Set up listener
     */
    fun setOnViewStubListener(listener: OnViewStubListener?) {
        this.listener = listener
    }

    interface OnViewStubListener {

        /**
         * Layout fill callback (View initialization can be done here)
         *
         * @param stub current ViewStub object
         * @param inflatedView filled layout object
         */
        fun onInflate(stub: CustomViewStub, inflatedView: View)

        /**
         * Visible state changes (View updates can be done here)
         *
         * @param stub current ViewStub object
         * @param visibility Visible status parameter changes
         */
        fun onVisibility(stub: CustomViewStub, visibility: Int)
    }
}