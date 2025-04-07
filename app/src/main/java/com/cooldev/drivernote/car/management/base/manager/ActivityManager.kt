package com.dhug.example.base.manager

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.collection.ArrayMap
import timber.log.Timber
import java.util.*

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class ActivityManager private constructor() : ActivityLifecycleCallbacks {

    companion object {

        @Suppress("StaticFieldLeak")
        private val activityManager: ActivityManager by lazy { ActivityManager() }

        fun getInstance(): ActivityManager {
            return activityManager
        }

        /**
         * Get the unique tag of an object
         */
        private fun getObjectTag(`object`: Any): String {
            //The package name of the object + the memory address of the object
            return `object`.javaClass.name + Integer.toHexString(`object`.hashCode())
        }
    }

   /** Activity storage collection */
    private val activitySet: ArrayMap<String?, Activity?> = ArrayMap()

    /** Application life cycle callback */
    private val lifecycleCallbacks: ArrayList<ApplicationLifecycleCallback> = ArrayList()

    /** Current application context object */
    private lateinit var application: Application

    /** Activity object on the top of the stack */
    private var topActivity: Activity? = null

    /** Foreground and visible Activity object */
    private var resumedActivity: Activity? = null

    fun init(application: Application) {
        this.application = application
        this.application.registerActivityLifecycleCallbacks(this)
    }

    /**
     * Get the Application object
     */
    fun getApplication(): Application {
        return application
    }

    /**
     * Get the Activity on the top of the stack
     */
    fun getTopActivity(): Activity? {
        return topActivity
    }

    /**
     * Get the activity that is in the foreground and visible
     */
    fun getResumedActivity(): Activity? {
        return resumedActivity
    }

    /**
     * Determine whether the current application is in the foreground state
     */
    fun isForeground(): Boolean {
        return getResumedActivity() != null
    }

    /**
     * Register application life cycle callbacks
     */
    fun registerApplicationLifecycleCallback(callback: ApplicationLifecycleCallback) {
        lifecycleCallbacks.add(callback)
    }

    /**
     * Unregister application life cycle callback
     */
    fun unregisterApplicationLifecycleCallback(callback: ApplicationLifecycleCallback) {
        lifecycleCallbacks.remove(callback)
    }

    /**
     * Destroy the specified Activity
     */
    fun finishActivity(clazz: Class<out Activity?>?) {
        if (clazz == null) {
            return
        }
        val keys: Array<String?> = activitySet.keys.toTypedArray()
        for (key: String? in keys) {
            val activity: Activity? = activitySet[key]
            if (activity == null || activity.isFinishing) {
                continue
            }
            if ((activity.javaClass == clazz)) {
                activity.finish()
                activitySet.remove(key)
                break
            }
        }
    }

    /**
     * Destroy all activities
     */
    fun finishAllActivities() {
        finishAllActivities(null as Class<out Activity?>?)
    }

    /**
     * Destroy all activities
     *
     * @param classArray whitelist Activity
     */
    @SafeVarargs
    fun finishAllActivities(vararg classArray: Class<out Activity>?) {
        val keys: Array<String?> = activitySet.keys.toTypedArray()
        for (key: String? in keys) {
            val activity: Activity? = activitySet[key]
            if (activity == null || activity.isFinishing) {
                continue
            }
            var whiteClazz = false
            for (clazz: Class<out Activity?>? in classArray) {
                if ((activity.javaClass == clazz)) {
                    whiteClazz = true
                }
            }
            if (whiteClazz) {
                continue
            }

            // If the activity is not on the whitelist, it will be destroyed.
            activity.finish()
            activitySet.remove(key)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("%s - onCreate", activity.javaClass.simpleName)
        if (activitySet.size == 0) {
            for (callback: ApplicationLifecycleCallback? in lifecycleCallbacks) {
                callback?.onApplicationCreate(activity)
            }
            Timber.i("%s - onApplicationCreate", activity.javaClass.simpleName)
        }
        activitySet[getObjectTag(activity)] = activity
        topActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("%s - onStart", activity.javaClass.simpleName)
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("%s - onResume", activity.javaClass.simpleName)
        if (topActivity === activity && resumedActivity == null) {
            for (callback: ApplicationLifecycleCallback in lifecycleCallbacks) {
                callback.onApplicationForeground(activity)
            }
            Timber.i("%s - onApplicationForeground", activity.javaClass.simpleName)
        }
        topActivity = activity
        resumedActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("%s - onPause", activity.javaClass.simpleName)
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("%s - onStop", activity.javaClass.simpleName)
        if (resumedActivity === activity) {
            resumedActivity = null
        }
        if (resumedActivity == null) {
            for (callback: ApplicationLifecycleCallback in lifecycleCallbacks) {
                callback.onApplicationBackground(activity)
            }
            Timber.i("%s - onApplicationBackground", activity.javaClass.simpleName)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.i("%s - onSaveInstanceState", activity.javaClass.simpleName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("%s - onDestroy", activity.javaClass.simpleName)
        activitySet.remove(getObjectTag(activity))
        if (topActivity === activity) {
            topActivity = null
        }
        if (activitySet.size == 0) {
            for (callback: ApplicationLifecycleCallback in lifecycleCallbacks) {
                callback.onApplicationDestroy(activity)
            }
            Timber.i("%s - onApplicationDestroy", activity.javaClass.simpleName)
        }
    }

   /**
     * Application life cycle callback
     */
    interface ApplicationLifecycleCallback {

        /**
         * The first Activity is created
         */
        fun onApplicationCreate(activity: Activity)

        /**
         * The last Activity is destroyed
         */
        fun onApplicationDestroy(activity: Activity)

        /**
         * The application enters the background from the foreground
         */
        fun onApplicationBackground(activity: Activity)

        /**
         * The application enters the foreground from the background
         */
        fun onApplicationForeground(activity: Activity)
    }
}