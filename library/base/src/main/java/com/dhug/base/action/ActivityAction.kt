package com.dhug.base.action

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
interface ActivityAction {

    /**
     * Get the Context object
     */
    fun getContext(): Context

    /**
     * Get Activity object
     */
    fun getActivity(): Activity? {
        var context: Context? = getContext()
        do {
            when (context) {
                is Activity -> {
                    return context
                }
                is ContextWrapper -> {
                    context = context.baseContext
                }
                else -> {
                    return null
                }
            }
        } while (context != null)
        return null
    }

    /**
     * Jump to activity simplified version
     */
    fun startActivity(clazz: Class<out Activity>) {
        startActivity(Intent(getContext(), clazz))
    }

    /**
     * Jump Activity
     */
    fun startActivity(intent: Intent) {
        if (getContext() !is Activity) {
            // If the current context is not Activity, calling startActivity must add the mark of the new task stack, otherwise an error will be reported: android.util.AndroidRuntimeException
            // Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getContext().startActivity(intent)
    }
}