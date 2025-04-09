package com.dhug.base.wiget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.dhug.base.BaseConstants
import com.cooldev.base.databinding.AdsViewBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.*
import kotlin.collections.iterator

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 11 / 01 / 2025
 */

class AdsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = AdsViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var adView: AdView? = null
    private var adUnitId: String? = null

    init {
        MobileAds.initialize(context) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for ((key, status) in statusMap) {
                Log.d("AdsView", "Adapter: $key, Status: ${status.description}")
            }
//            loadAd()
        }
    }

    fun setAdUnitId(adUnitId: String) {
        this.adUnitId = adUnitId
    }

    fun loadAd() {
        Log.d("AdManager Init Banner", "Ad Init and Loading...")
        showAd()
        if (adView != null) return

        val validAdUnitId = adUnitId ?: BaseConstants.getAdmobAppIdBanner()
        if (validAdUnitId.isEmpty()) {
            Log.e("AdsView", "AdUnitId is empty. Cannot load ad.")
            return
        }
        Log.e("AdsView", "AdUnitId $validAdUnitId")

        // Calculate adaptive banner size
        val adSize = getAdaptiveBannerAdSize()

        // Create AdView
        adView = AdView(context).apply {
            this.adUnitId = validAdUnitId
            setAdSize(adSize)
        }

        // Add AdView to adContainer
        binding.adContainer.addView(adView)

        // Request to load ad
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)

        // Listen for ad status
        adView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Ad loaded successfully
                Log.d("AdsView", "Ad loaded successfully")
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = GONE
                adView?.visibility = VISIBLE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load
                Log.d("AdsView", "Ad failed to load: ${adError.message}")
                binding.shimmerLayout.stopShimmer()
            }
        }
    }

    private fun getAdaptiveBannerAdSize(): AdSize {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)
    }

    fun hideAd() {
        binding.root.visibility = GONE
    }

    fun showAd() {
        binding.root.visibility = VISIBLE
    }

    fun destroyAd() {
        adView?.destroy()
        adView = null
        hideAd()
    }
}