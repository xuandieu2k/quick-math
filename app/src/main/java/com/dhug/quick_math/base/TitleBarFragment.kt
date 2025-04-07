package com.dhug.quick_math.base

import android.os.Bundle
import android.view.*
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.TitleBar
import com.dhug.quick_math.R
import com.dhug.quick_math.base.action.TitleBarAction

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
abstract class TitleBarFragment : AppFragment(), TitleBarAction {

    /** Title bar object */
    private var titleBar: TitleBar? = null

    /** Status bar immersion */
    private var immersionBar: ImmersionBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleBar = getTitleBar()
        //Set title bar click listening
        titleBar?.setOnTitleBarListener(this)

        if (isStatusBarEnabled()) {
            //Initialize the immersive status bar
            getStatusBarConfig().init()
            if (titleBar != null) {
                //Set title bar immersion
                ImmersionBar.setTitleBar(this, titleBar)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isStatusBarEnabled()) {
            // Reinitialize the status bar
            getStatusBarConfig().init()
        }
    }

   /**
     * Whether to use immersive in Fragment
     */
    open fun isStatusBarEnabled(): Boolean {
        return false
    }

    /**
     * Get the status bar immersion configuration object
     */
    protected fun getStatusBarConfig(): ImmersionBar {
        if (immersionBar == null) {
            immersionBar = createStatusBarConfig()
        }
        return immersionBar!!
    }

    /**
     * Initialize immersive
     */
    protected fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            //The default status bar font color is black
            .statusBarDarkFont(isStatusBarDarkFont())
            //Specify the background color of the navigation bar
            .navigationBarColor(R.color.white)
            //The status bar font and navigation bar content will automatically change color. You must specify the status bar color and navigation bar color before they can automatically change color.
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * Get status bar font color
     */
    protected open fun isStatusBarDarkFont(): Boolean {
        //Return true to indicate black font
        return (getAttachActivity() as? AppActivity)!!.isStatusBarDarkFont()
    }

    override fun getTitleBar(): TitleBar? {
        if (titleBar == null || !isLoading()) {
            titleBar = obtainTitleBar(view as ViewGroup)
        }
        return titleBar
    }
}