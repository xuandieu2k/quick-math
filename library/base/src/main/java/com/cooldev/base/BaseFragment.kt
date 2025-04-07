package com.cooldev.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.cooldev.base.BaseActivity.OnActivityCallback
import com.cooldev.base.action.BundleAction
import com.cooldev.base.action.ClickAction
import com.cooldev.base.action.HandlerAction
import com.cooldev.base.action.KeyboardAction
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */

@AndroidEntryPoint
abstract class BaseFragment : Fragment(),
    HandlerAction, ClickAction, BundleAction, KeyboardAction {

    /** Activity object */
    private var activity: BaseActivity? = null

    /** Root layout */
    private var rootView: View? = null

    /** Whether it has been loaded currently */
    private var loading: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Get the global Activity
        activity = requireActivity() as? BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getLayoutView() == null) {
            return null
        }
        loading = false
        rootView = getLayoutView()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (!loading) {
            loading = true
            initData()
            observerData()
            onFragmentResume(true)
            return
        }

        if (this.activity?.lifecycle?.currentState == Lifecycle.State.STARTED) {
            onActivityResume()
        } else {
            onFragmentResume(false)
        }
    }

    /**
     * Fragment visible callback
     *
     * @param first whether it is called for the first time
     */
    protected open fun onFragmentResume(first: Boolean) {}

    /**
     * Activity visible callback
     */
    protected open fun onActivityResume() {}

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        loading = false
        removeCallbacks()
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    /**
     * Whether this Fragment has been loaded?
     */
    open fun isLoading(): Boolean {
        return loading
    }

    override fun getView(): View? {
        return rootView
    }

    /**
     * Get the bound Activity to prevent getActivity from being empty
     */
    open fun getAttachActivity(): BaseActivity? {
        return activity
    }

    /**
     * Get the Application object
     */
    open fun getApplication(): Application? {
        activity?.let { return it.application }
        return null
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

    /**
     * Get a View object based on resource id
     */
    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return rootView?.findViewById(id)
    }

    override fun getBundle(): Bundle? {
        return arguments
    }

    /**
     * Jump to activity simplified version
     */
    open fun startActivity(clazz: Class<out Activity>) {
        startActivity(Intent(context, clazz))
    }

    /**
     * startActivityForResult method optimization
     */
    open fun startActivityForResult(clazz: Class<out Activity>, callback: OnActivityCallback?) {
        activity?.startActivityForResult(clazz, callback)
    }

    open fun startActivityForResult(intent: Intent, callback: OnActivityCallback?) {
        activity?.startActivityForResult(intent, null, callback)
    }

    open fun startActivityForResult(intent: Intent, options: Bundle?, callback: OnActivityCallback?) {
        activity?.startActivityForResult(intent, options, callback)
    }

    /**
     * Destroy the Activity where the current Fragment is located
     */
    open fun finish() {
        this.activity?.let {
            if (it.isFinishing || it.isDestroyed) {
                return
            }
            it.finish()
        }
    }

    /**
     * Fragment key event dispatch
     */
    open fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val fragments: MutableList<Fragment?> = childFragmentManager.fragments
        for (fragment: Fragment? in fragments) {
            // This sub-Fragment must be a subclass of BaseFragment and be visible
            if (fragment !is BaseFragment || fragment.lifecycle.currentState != Lifecycle.State.RESUMED) {
                continue
            }
            // Dispatch the key event to the sub-Fragment for processing
            if (fragment.dispatchKeyEvent(event)) {
                // If the child Fragment intercepts this event, it will not be handed over to the parent Fragment for processing.
                return true
            }
        }
        return when (event?.action) {
            KeyEvent.ACTION_DOWN -> onKeyDown(event.keyCode, event)
            KeyEvent.ACTION_UP -> onKeyUp(event.keyCode, event)
            else -> false
        }
    }

    /**
     * Button press event callback
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //Do not intercept key events by default
        return false
    }

    /**
     * Button lift event callback
     */
    open fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        //Do not intercept key events by default
        return false
    }

    override fun getContext(): Context? {
        return activity
    }
}