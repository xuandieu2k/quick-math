package com.dhug.base.action

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.dhug.base.R

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
interface ClickAction : View.OnClickListener {

    fun <V : View?> findViewById(@IdRes id: Int): V?

    companion object {
        const val TAG_CLICK_ADS = 49090033
    }

    // ===== CLICK THƯỜNG =====
    fun setOnClickListener(@IdRes vararg ids: Int) {
        setOnClickListener(this, *ids)
    }

    fun setOnClickListener(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (id in ids) {
            findViewById<View?>(id)?.apply {
                setTag(TAG_CLICK_ADS, false)
                setOnClickListener(listener)
            }
        }
    }

    fun setOnClickListener(vararg views: View?) {
        setOnClickListener(this, *views)
    }

    fun setOnClickListener(listener: View.OnClickListener?, vararg views: View?) {
        for (view in views) {
            view?.apply {
                setTag(TAG_CLICK_ADS, false)
                setOnClickListener(listener)
            }
        }
    }

    // ===== CLICK CÓ ADS =====
    fun setOnClickListenerAds(@IdRes vararg ids: Int) {
        setOnClickListenerAds(this, *ids)
    }

    fun setOnClickListenerAds(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (id in ids) {
            findViewById<View?>(id)?.apply {
                setTag(TAG_CLICK_ADS, true)
                setOnClickListener(listener)
            }
        }
    }

    fun setOnClickListenerAds(vararg views: View?) {
        setOnClickListenerAds(this, *views)
    }

    fun setOnClickListenerAds(listener: View.OnClickListener?, vararg views: View?) {
        for (view in views) {
            view?.apply {
                setTag(TAG_CLICK_ADS, true)
                setOnClickListener(listener)
            }
        }
    }

    // ===== CLICK CHO TẤT CẢ CHILD VIEW =====
    fun setChildOnClickListener(@IdRes vararg parentIds: Int) {
        setChildOnClickListener(this, *parentIds)
    }

    fun setChildOnClickListener(listener: View.OnClickListener?, @IdRes vararg parentIds: Int) {
        for (parentId in parentIds) {
            findViewById<View?>(parentId)?.setChildOnClickListener(listener ?: this)
        }
    }

    fun setChildOnClickListener(vararg parents: View) {
        setChildOnClickListener(this, *parents)
    }

    fun setChildOnClickListener(listener: View.OnClickListener?, vararg parents: View) {
        for (parent in parents) {
            parent.setChildOnClickListener(listener ?: this)
        }
    }

    // ===== CLICK CHO TẤT CẢ CHILD VIEW CÓ ADS =====
    fun setChildOnClickListenerAds(@IdRes vararg parentIds: Int) {
        setChildOnClickListenerAds(this, *parentIds)
    }

    fun setChildOnClickListenerAds(listener: View.OnClickListener?, @IdRes vararg parentIds: Int) {
        for (parentId in parentIds) {
            findViewById<View?>(parentId)?.setChildOnClickListenerAds(listener ?: this)
        }
    }

    fun setChildOnClickListenerAds(vararg parents: View) {
        setChildOnClickListenerAds(this, *parents)
    }

    fun setChildOnClickListenerAds(listener: View.OnClickListener?, vararg parents: View) {
        for (parent in parents) {
            parent.setChildOnClickListenerAds(listener ?: this)
        }
    }

    // ===== EXTENSION CHO VIEW =====
    fun View.setChildOnClickListener(listener: View.OnClickListener) {
        setTag(TAG_CLICK_ADS, false)
        setOnClickListener { listener.onClick(this) }
    }

    fun View.setChildOnClickListenerAds(listener: View.OnClickListener) {
        setTag(TAG_CLICK_ADS, true)
        setOnClickListener(listener)
    }

    fun View.setOnClickListenerAds(listener: View.OnClickListener) {
        setTag(TAG_CLICK_ADS, true)
        setOnClickListener {
            listener.onClick(this)
        }
    }

    override fun onClick(view: View) {
        val isAdClick = view.getTag(TAG_CLICK_ADS) as? Boolean ?: false
        if (isAdClick) {
            onClickAds(view)
        } else {
            onClickNormal(view)
        }
    }

    fun onClickNormal(view: View) {
        // Xử lý click bình thường (override ở Activity/Fragment)
    }

    fun onClickAds(view: View) {
        // Xử lý click sau khi xem quảng cáo (override ở Activity/Fragment)
    }
}