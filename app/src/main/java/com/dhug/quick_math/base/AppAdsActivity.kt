package com.dhug.quick_math.base

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.dhug.quick_math.data.ads.InterstitialAdManager
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.TitleBar
import com.cooldev.base.BaseAdsActivity
import com.cooldev.base.BaseDialog
import com.dhug.quick_math.R
import com.dhug.quick_math.base.action.TitleBarAction
import com.dhug.quick_math.base.ui.dialog.WaitDialog
import com.dhug.quick_math.data.ads.OpenAdManager
import com.dhug.quick_math.presentation.viewmodel.PremiumViewModel
import com.dhug.quick_math.presentation.viewmodel.RemoteConfigViewModel
import com.dhug.quick_math.utils.ExtensionUtils
import com.dhug.quick_math.utils.MMKVUtils
import com.dhug.quick_math.utils.RemoteConfigConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class AppAdsActivity : BaseAdsActivity(), TitleBarAction {

    @Inject
    lateinit var openAdManager: OpenAdManager

    /** Title bar object */
    private var titleBar: TitleBar? = null

    /** Status bar immersion */
    private var immersionBar: ImmersionBar? = null

    /** Loading dialog box */
    private var dialog: BaseDialog? = null

    /** Number of dialog boxes */
    private var dialogCount: Int = 0

    val remoteConfigViewModel: RemoteConfigViewModel by viewModels()
    val premiumViewModel: PremiumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (MMKVUtils.areAnyPremiumsActive()) {
            removeAds()
            showAds(false)
            return
        }
    }

    override fun updateIdBanner(): String? = null

    override fun isHasBannerAd(): Boolean = MMKVUtils.isShowBannerAd()

    override fun onClickAds(view: View) {
        ExtensionUtils.showInterAd(this) {
            onClickAfterAd(view)
        }
    }

    /**
     * Click show inter ad
     */
    abstract fun onClickAfterAd(view: View)

    override fun observerData() {
        registerObserve()
//        premiumViewModel.isPremium.observe(this) {
//            when (it) {
//                is Resource.Error -> {}
//                is Resource.Loading -> {}
//                is Resource.Success -> {
//                    it.data?.let {
//                        Timber.tag("Log Premium").d("Yeah...")
//                        if (!it.second) return@let
//                        if (!it.first) {
//                            Timber.tag("Log Premium").d("Reopen ad")
//                            registerObserve()
//                            reOpenAds()
//                        } else {
//                            Timber.tag("Log Premium").d("Clear ad")
//                            remoteConfigViewModel.configData.removeObservers(this)
//                            destroyAds()
//                        }
//                    }
//                }
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        InterstitialAdManager.initDialog(this)
        premiumViewModel.refreshPurchase(
            onDone = {
                updateAds()
            },
            onError = {
                updateAds()
            }
        )
    }

    private fun hasBannerAd(): Boolean {
        return when (this) {
//            is OnboardingActivity -> {
//                MMKVUtils.getRemoteConfig().isShowAdsBannerOnboarding
//            }

            else -> MMKVUtils.getRemoteConfig().isShowAdsBanner
        }
    }

    private fun updateAds() {
        if (!MMKVUtils.areAnyPremiumsActive()) {
            if (hasBannerAd()) {
                if (isAdVisible()) return // Return when ad showing
                loadAds()
                showAds(true)
            } else {
                if (InterstitialAdManager.getInterstitialAd() != null) {
                    InterstitialAdManager.destroyAd()
                }
                removeAds()
                showAds(false)
            }
        } else {
            if (InterstitialAdManager.getInterstitialAd() != null) {
                InterstitialAdManager.destroyAd()
            }
            removeAds()
            showAds(false)
        }
    }

    private fun reOpenAds() {
        if (InterstitialAdManager.getInterstitialAd() == null ) { // && this !is OnboardingActivity
            InterstitialAdManager.initialize(
                this.application, MMKVUtils.getRemoteConfig().maxInterAdsCount.toInt(),
                MMKVUtils.getRemoteConfig().interstitialInterval
            )
        }

        if (openAdManager.getOpenAd() == null) { //  && this !is OnboardingActivity
            openAdManager.registerObserver()
            openAdManager.loadAd()
            openAdManager.updateCurrentActivity(this)

            if (hasBannerAd()) {
                loadAds()
                showAds(true)
            }
        }
    }

    private fun destroyAds() {
        removeAds()
        showAds(isShow = false)
        InterstitialAdManager.destroyAd()
        openAdManager.resetOpenAd()
    }

    private fun registerObserve() {
        if (!remoteConfigViewModel.configData.hasObservers()) {
            remoteConfigViewModel.configData.observe(this) { configData ->
                if (MMKVUtils.areAnyPremiumsActive()) {
                    return@observe
                }
                val remoteConfig = RemoteConfigConstants.getRemoteConfigByHashmap(configData)
//                val isShowAdsBanner =
//                    if (this is OnboardingActivity) remoteConfig.isShowAdsBannerOnboarding else remoteConfig.isShowAdsBanner
//                if (isShowAdsBanner) {
//                    loadAds()
//                } else {
//                    removeAds()
//                }
//                showAds(isShowAdsBanner)
//                if (remoteConfig.isShowAdsInterstitial) InterstitialAdManager.startAdControlAuto(
//                    this
//                ) else InterstitialAdManager.stopAdControl()
            }
        }

        // Lấy dữ liệu Remote Config
        remoteConfigViewModel.refreshConfig()
    }

    /**
     * Whether the current loading dialog box is being displayed
     */
    open fun isShowDialog(): Boolean {
        return dialog != null && dialog!!.isShowing
    }

    /**
     * Show loading dialog
     */
    open fun showDialog() {
        if (isFinishing || isDestroyed) {
            return
        }
        dialogCount++
        postDelayed(Runnable {
            if ((dialogCount <= 0) || isFinishing || isDestroyed) {
                return@Runnable
            }
            if (dialog == null) {
                dialog = WaitDialog.Builder(this)
                    .setCancelable(false)
                    .create()
            }
            if (!dialog!!.isShowing) {
                dialog!!.show()
            }
        }, 300)
    }

    /**
     * Hide loading dialog
     */
    open fun hideDialog() {
        if (isFinishing || isDestroyed) {
            return
        }
        if (dialogCount > 0) {
            dialogCount--
        }
        if ((dialogCount != 0) || (dialog == null) || !dialog!!.isShowing) {
            return
        }
        dialog?.dismiss()
    }

    override fun initLayout() {
        super.initLayout()

        val titleBar = getTitleBar()
        titleBar?.setOnTitleBarListener(this)

        //Initialize the immersive status bar
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()

            //Set title bar immersion
            if (titleBar != null) {
                ImmersionBar.setTitleBar(this, titleBar)
            }
        }
    }

    /**
     * Whether to use immersive status bar
     */
    protected open fun isStatusBarEnabled(): Boolean {
        return true
    }

    /**
     * Status bar font dark mode
     */
    open fun isStatusBarDarkFont(): Boolean {
        return true
    }

    /**
     * Get the status bar immersion configuration object
     */
    open fun getStatusBarConfig(): ImmersionBar {
        if (immersionBar == null) {
            immersionBar = createStatusBarConfig()
        }
        return immersionBar!!
    }

    /**
     * Initialize the immersive status bar
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this) //The default status bar font color is black
            .statusBarDarkFont(isStatusBarDarkFont()) //Specify the navigation bar background color
            .navigationBarColor(R.color.white) // The status bar font and navigation bar content automatically change color. You must specify the status bar color and navigation bar color before they can automatically change color.
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * Set the title of the title bar
     */
    override fun setTitle(@StringRes id: Int) {
        title = getString(id)
    }

    /**
     * Set the title of the title bar
     */
    override fun setTitle(title: CharSequence?) {
        super<BaseAdsActivity>.setTitle(title)
        getTitleBar()?.title = title
    }

    override fun getTitleBar(): TitleBar? {
        if (titleBar == null) {
            titleBar = obtainTitleBar(getContentView())
        }
        return titleBar
    }

    override fun onLeftClick(view: View) {
        @Suppress("DEPRECATION")
        onBackPressed()
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity)
    }

    override fun finish() {
        super.finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowDialog()) {
            hideDialog()
        }
        dialog = null
    }

    /**
     * View touch out
     */
    private val mTouchOutsideViews: MutableList<View> = mutableListOf()

    /**
     * Listener touch out
     */
    private var mOnTouchOutsideViewListener: OnTouchOutsideViewListener? = null

    /**
     * Sets a listener that is notified when the user taps outside the given views.
     * To remove the listener, call [removeOnTouchOutsideViewListener].
     *
     * @param views The list of views to monitor.
     * @param onTouchOutsideViewListener The listener to notify when a touch outside occurs.
     */
    fun setOnTouchOutsideViewListener(
        views: List<View>, onTouchOutsideViewListener: OnTouchOutsideViewListener
    ) {
        mTouchOutsideViews.clear()
        mTouchOutsideViews.addAll(views)
        mOnTouchOutsideViewListener = onTouchOutsideViewListener
    }

    /**
     * Get the current touch outside listener.
     */
    fun getOnTouchOutsideViewListener(): OnTouchOutsideViewListener? {
        return mOnTouchOutsideViewListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (mOnTouchOutsideViewListener != null) {
                val isOutside = mTouchOutsideViews.all { view ->
                    if (view.visibility == View.VISIBLE) {
                        val viewRect = Rect()
                        view.getGlobalVisibleRect(viewRect)
                        !viewRect.contains(ev.rawX.toInt(), ev.rawY.toInt())
                    } else {
                        true
                    }
                }

                if (isOutside) {
                    mOnTouchOutsideViewListener?.onTouchOutside(mTouchOutsideViews, ev)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Interface definition for a callback to be invoked when a touch event has occurred outside the specified views.
     */
    interface OnTouchOutsideViewListener {
        /**
         * Called when a touch event has occurred outside the given views.
         *
         * @param views The list of views that were not touched.
         * @param event The MotionEvent object containing full information about the event.
         */
        fun onTouchOutside(views: List<View>, event: MotionEvent)
    }

}