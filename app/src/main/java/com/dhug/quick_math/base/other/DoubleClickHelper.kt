package com.dhug.quick_math.base.other

import android.os.SystemClock

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 双击判断工具类
 */
object DoubleClickHelper {

    /** The length of the array is 2, which means that only double-click operations are recorded */
    private val TIME_ARRAY: LongArray = LongArray(2)

    /**
     * Whether a double-click operation was performed within a short period of time
     */
    fun isOnDoubleClick(): Boolean {
        //Default interval length
        return isOnDoubleClick(1500)
    }

    /**
     * Whether a double-click operation was performed within a short period of time
     */
    fun isOnDoubleClick(time: Int): Boolean {
        System.arraycopy(TIME_ARRAY, 1, TIME_ARRAY, 0, TIME_ARRAY.size - 1)
        TIME_ARRAY[TIME_ARRAY.size - 1] = SystemClock.uptimeMillis()
        return TIME_ARRAY[0] >= (SystemClock.uptimeMillis() - time)
    }
}