package com.dhug.example.base.aop

import android.app.Activity
import com.dhug.example.base.other.PermissionCallback
import com.hjq.permissions.XXPermissions
//import com.tencent.bugly.crashreport.CrashReport
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import timber.log.Timber
import com.dhug.example.base.manager.ActivityManager

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22/10/2022
 */
@Suppress("unused")
@Aspect
class PermissionsAspect {

    /**
     *Method entry point
     */
    @Pointcut("execution(@vn.cooldev.dashcam.base.aop.Permissions * *(..))")
    fun method() {}

    /**
     * Method substitution at connection point
     */
    @Around("method() && @annotation(permissions)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permissions: Permissions) {
        var activity: Activity? = null

        //Method parameter value collection
        val parameterValues: Array<Any?> = joinPoint.args
        for (arg: Any? in parameterValues) {
            if (arg !is Activity) {
                continue
            }
            activity = arg
            break
        }
        if ((activity == null) || activity.isFinishing || activity.isDestroyed) {
            activity = ActivityManager.getInstance().getTopActivity()
        }
        if ((activity == null) || activity.isFinishing || activity.isDestroyed) {
            Timber.e("The activity has been destroyed and permission requests cannot be made")
            return
        }
        requestPermissions(joinPoint, activity, permissions.value)
    }

    private fun requestPermissions(joinPoint: ProceedingJoinPoint, activity: Activity, permissions: Array<out String>) {
        XXPermissions.with(activity)
            .permission(*permissions)
            .request(object : PermissionCallback() {
                override fun onGranted(permissions: MutableList<String?>?, all: Boolean) {
                    if (all) {
                        try {
                            // Obtain permission and execute the original method
                            joinPoint.proceed()
                        } catch (e: Throwable) {
//                            CrashReport.postCatchedException(e)
                        }
                    }
                }
            })
    }
}