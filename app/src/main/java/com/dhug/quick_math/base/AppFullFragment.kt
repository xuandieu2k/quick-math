package com.dhug.quick_math.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.cooldev.base.action.BundleAction
import com.cooldev.base.action.ClickAction
import com.cooldev.base.action.HandlerAction
import com.cooldev.base.action.KeyboardAction
import com.dhug.quick_math.utils.ExtensionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class AppFullFragment : Fragment(), HandlerAction, ClickAction, BundleAction,
    KeyboardAction {

    /** Root layout */
    private var rootView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        observerData()
    }

    override fun onClickAds(view: View) {
        ExtensionUtils.showInterAd(requireActivity()) {
            onClickAfterAd(view)
        }
    }

    /**
     * Handle event click after inter ad is showed
     * By pass if you don't want to show inter ad
     */
    abstract fun onClickAfterAd(view: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getLayoutView() == null) {
            return null
        }
        rootView = getLayoutView()
        return rootView
    }

    /**
     * Get layout view
     */
    protected abstract fun getLayoutView(): View?

    /**
     * Initialize the control
     */
    protected abstract fun initView()

    /**
     * Initialization data
     */
    protected abstract fun initData()

    /**
     *Initialization observerData
     */
    protected abstract fun observerData()

    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return rootView?.findViewById(id)
    }

    override fun getBundle(): Bundle? {
        return arguments
    }
}