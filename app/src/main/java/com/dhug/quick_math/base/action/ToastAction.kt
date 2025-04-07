package com.dhug.quick_math.base.action

import androidx.annotation.StringRes
import com.hjq.toast.ToastUtils

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
interface ToastAction {

    fun toast(text: CharSequence?) {
        ToastUtils.show(text)
    }

    fun toast(@StringRes id: Int) {
        ToastUtils.show(id)
    }

    fun toast(`object`: Any?) {
        ToastUtils.show(`object`)
    }
}