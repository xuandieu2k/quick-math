package com.cooldev.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.cooldev.base.action.ActivityAction
import com.cooldev.base.action.ClickAction
import com.cooldev.base.action.HandlerAction
import com.cooldev.base.action.KeyboardAction
import com.cooldev.base.action.BundleAction
import java.util.*
import kotlin.math.pow

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
abstract class BaseActivity : AppCompatActivity(), ActivityAction,
    ClickAction, HandlerAction, BundleAction, KeyboardAction {

    companion object {

        /** Error result code */
        const val RESULT_ERROR: Int = -2
    }

    /** Activity callback collection */
    private val activityCallbacks: SparseArray<OnActivityCallback?> by lazy { SparseArray(1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        hideBottomNavigationBar()
    }

    private fun hideBottomNavigationBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.navigationBars())
                window.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     *  Utility to check if the app is in the foreground
     */
    fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (process in appProcesses) {
            if (process.processName == context.packageName &&
                process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return true
            }
        }
        return false
    }

    protected open fun initActivity() {
        initLayout()
        initView()
        initData()
        observerData()
    }

    /**
     * Get layout ID
     */
    protected abstract fun getLayoutView(): View

    /**
     * Initialize the control
     */
    protected abstract fun initView()

    /**
     *Initialization data
     */
    protected abstract fun initData()

    /**
     *Initialization observerData
     */
    protected abstract fun observerData()

    /**
     * Initialize layout
     */
    protected open fun initLayout() {
        setContentView(getLayoutView())
        initSoftKeyboard()
    }

    /**
     * Initialize soft keyboard
     */
    protected open fun initSoftKeyboard() {
        // Click outside to hide the soft keyboard to improve user experience
        getContentView()?.setOnClickListener {
            //Hide soft keys to avoid memory leaks
            hideKeyboard(currentFocus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallbacks()
    }

    override fun finish() {
        super.finish()
        //Hide soft keys to avoid memory leaks
        hideKeyboard(currentFocus)
    }

    /**
     * Will be called back if the current Activity (singleTop startup mode) is reused
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //Set as the current Intent to prevent the Activity from being restarted after being killed and the Intent remains the original one.
        setIntent(intent)
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    /**
     * Method corresponding to setContentView
     */
    open fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }

    override fun getContext(): Context {
        return this
    }

    override fun startActivity(intent: Intent) {
        return super<AppCompatActivity>.startActivity(intent)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val fragments: MutableList<Fragment?> = supportFragmentManager.fragments
        for (fragment: Fragment? in fragments) {
            // This Fragment must be a subclass of BaseFragment and be visible
            if (fragment !is BaseFragment || fragment.lifecycle.currentState != Lifecycle.State.RESUMED) {
                continue
            }
            // Dispatch key events to Fragment for processing
            if (fragment.dispatchKeyEvent(event)) {
                // If Fragment intercepts this event, it will not be handed over to Activity for processing
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    @Suppress("deprecation")
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        //Hide soft keys to avoid memory leaks
        hideKeyboard(currentFocus)
        // Check the source code to know that startActivity will eventually call startActivityForResult
        super.startActivityForResult(intent, requestCode, options)
    }

    /**
     * startActivityForResult method optimization
     */
    open fun startActivityForResult(clazz: Class<out Activity>, callback: OnActivityCallback?) {
        startActivityForResult(Intent(this, clazz), null, callback)
    }

    open fun startActivityForResult(intent: Intent, callback: OnActivityCallback?) {
        startActivityForResult(intent, null, callback)
    }

    @Suppress("deprecation")
    open fun startActivityForResult(
        intent: Intent,
        options: Bundle?,
        callback: OnActivityCallback?
    ) {
        //The request code must be within 2 to the 16th power
        val requestCode: Int = Random().nextInt(2.0.pow(16.0).toInt())
        activityCallbacks.put(requestCode, callback)
        startActivityForResult(intent, requestCode, options)
    }

    @Suppress("deprecation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var callback: OnActivityCallback?
        if ((activityCallbacks.get(requestCode).also { callback = it }) != null) {
            callback?.onActivityResult(resultCode, data)
            activityCallbacks.remove(requestCode)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface OnActivityCallback {

        /**
         * Result callback
         *
         * @param resultCode result code
         * @param data data
         */
        fun onActivityResult(resultCode: Int, data: Intent?)
    }
}