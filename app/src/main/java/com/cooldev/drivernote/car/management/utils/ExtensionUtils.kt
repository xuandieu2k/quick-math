package com.dhug.example.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dhug.example.R
import com.dhug.example.data.ads.InterstitialAdManager
import timber.log.Timber
import java.util.LinkedList
import java.util.Queue

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 11 / 01 / 2025
 */
object ExtensionUtils {
    fun View.showAdsInterstitialOnClick(activity: Activity) {
        this.setOnClickListener {
            if (!MMKVUtils.getRemoteConfig().isShowAdsInterstitial) return@setOnClickListener
            if (MMKVUtils.getRemoteConfig().isShowAdsInterstitial) InterstitialAdManager.showAdOnClick(
                activity
            ) {

            }
        }
    }

    fun View.setMoreEventOnClick(activity: Activity, onClick: (View) -> Unit) {
        this.setOnClickListener {
            Timber.tag("OnClickView").d("Preparing Ads")
            if (!MMKVUtils.getRemoteConfig().isShowAdsInterstitial){
                onClick(it)
                return@setOnClickListener
            }
            if (MMKVUtils.getRemoteConfig().isShowAdsInterstitial) InterstitialAdManager.showAdOnClick(
                activity
            ) {
                onClick(it)
            }
        }
    }

    fun showInterAd(activity: Activity, onDone: () -> Unit) {
        if (!MMKVUtils.getRemoteConfig().isShowAdsInterstitial) {
            onDone()
            return
        }
        if (MMKVUtils.getRemoteConfig().isShowAdsInterstitial) InterstitialAdManager.showAdOnClick(
            activity
        ) {
            onDone()
        }
    }


    private var View.lastClickTime: Long
        get() = (getTag(R.id.last_click_time) as? Long) ?: 0L
        set(value) = setTag(R.id.last_click_time, value)

    private fun View.updateLastClickTime(time: Long) {
        setTag(R.id.last_click_time, time)
    }

    fun View.setDebouncedOnClickListener(
        debounceTime: Long = 500L,
        onClick: (View) -> Unit,
        activity: Activity
    ) {
        this.setOnClickListener { view ->
            showInterAd(activity){
                val currentTime = System.currentTimeMillis()
                if (currentTime - view.lastClickTime >= debounceTime) {
                    view.updateLastClickTime(currentTime)
                    onClick(view)
                }
            }
        }
    }

    fun Array<View>.setDebouncedOnClickListener(
        debounceTime: Long = 500L,
        onClick: (View) -> Unit,
        activity: Activity
    ) {
        forEach { view ->
            view.setDebouncedOnClickListener(debounceTime, onClick, activity)
        }
    }

    fun View.getAllChildIds(): List<Int> {
        val ids = mutableListOf<Int>()
        val queue: Queue<View> = LinkedList()
        queue.add(this) // Bắt đầu từ ViewGroup cha

        while (queue.isNotEmpty()) {
            val view = queue.poll()
            if (view != null) {
                if (view.id != View.NO_ID) {
                    ids.add(view.id) // Lưu ID nếu có
                }
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    queue.add(view.getChildAt(i)) // Thêm các con vào hàng đợi
                }
            }
        }
        return ids
    }

    fun <T> List<T>.hasSameSizeAndIds(other: List<T>, idSelector: (T) -> Any): Boolean {
        return this.size == other.size && this.map(idSelector).toSet() == other.map(idSelector).toSet()
    }

    @SuppressLint("DiscouragedApi")
    fun Context.getDrawableByName(name: String): Drawable? {
        val resId = resources.getIdentifier(name, "drawable", packageName)
        return if (resId != 0) ContextCompat.getDrawable(this, resId) else null
    }



}