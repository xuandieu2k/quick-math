@file:Suppress("DEPRECATION")

package com.dhug.example.base.aop

import android.app.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat
import com.hjq.toast.ToastUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import com.dhug.example.R
import com.dhug.example.base.manager.ActivityManager

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("unused")
@Aspect
class CheckNetAspect {

    /**
     *Method entry point
     */
    @Pointcut("execution(@vn.cooldev.dashcam.base.aop.CheckNet * *(..))")
    fun method() {}

    /**
     * Method substitution at connection point
     */
    @Around("method() && @annotation(checkNet)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, checkNet: CheckNet) {
        val application: Application = ActivityManager.getInstance().getApplication()
        val manager: ConnectivityManager? = ContextCompat.getSystemService(application, ConnectivityManager::class.java)
        if (manager != null) {
            @Suppress("DEPRECATION") val info: NetworkInfo? = manager.activeNetworkInfo
            // Determine whether the network is connected
            @Suppress("DEPRECATION")
            if (info == null || !info.isConnected) {
                ToastUtils.show(R.string.common_network_hint)
                return
            }
        }
        //Execute original method
        joinPoint.proceed()
    }
}