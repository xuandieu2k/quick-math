package vn.cooldev.wiget.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import vn.cooldev.wiget.R


/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 11 / 2024
 */
class MaxHeightRecyclerView : RecyclerView {
    private var mMaxHeight = 0

    constructor(context: Context?) : super(context!!)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initialize(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightScrollView_maxHeight, mMaxHeight)
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = heightMeasureSpec
        if (mMaxHeight > 0) {
            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}