package com.dhug.base.action

import android.os.Handler
import android.os.Looper
import android.os.SystemClock

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22/10/2022
 */
interface HandlerAction {

    companion object {
        val HANDLER: Handler = Handler(Looper.getMainLooper())
    }

    /**
     * Get Handler
     */
    fun getHandler(): Handler {
        return HANDLER
    }

    /**
     * Delayed execution
     */
    fun post(runnable: Runnable): Boolean {
        return postDelayed(runnable, 0)
    }

    /**
     * Delay execution for a period of time
     */
    fun postDelayed(runnable: Runnable, delayMillis: Long): Boolean {
        return postAtTime(runnable, SystemClock.uptimeMillis() + if (delayMillis < 0) 0 else delayMillis)
    }

    /**
     * Execute at the specified time
     */
    fun postAtTime(runnable: Runnable, uptimeMillis: Long): Boolean {
        //Send message callback related to the current object
        return HANDLER.postAtTime(runnable, this, uptimeMillis)
    }

    /**
     * Remove a single message callback
     */
    fun removeCallbacks(runnable: Runnable) {
        HANDLER.removeCallbacks(runnable)
    }

    /**
     * Remove all message callbacks
     */
    fun removeCallbacks() {
        // Remove the message callback related to the current object
        HANDLER.removeCallbacksAndMessages(this)
    }
}