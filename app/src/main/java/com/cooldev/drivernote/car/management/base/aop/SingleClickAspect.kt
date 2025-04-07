package com.dhug.example.base.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import timber.log.Timber

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("unused")
@Aspect
class SingleClickAspect {
/** The time of the most recent click */
    private var lastTime: Long = 0

    /** The most recent click mark */
    private var lastTag: String? = null

    /**
     *Method entry point
     */
    @Pointcut("execution(@vn.cooldev.dashcam.base.aop.SingleClick * *(..))")
    fun method() {}

    /**
     * Method substitution at connection point
     */
    @Around("method() && @annotation(singleClick)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, singleClick: SingleClick) {
        val codeSignature: CodeSignature = joinPoint.signature as CodeSignature
        //The class where the method is located
        val className: String = codeSignature.declaringType.name
        // method name
        val methodName: String = codeSignature.name
        //Build method TAG
        val builder: StringBuilder = StringBuilder("$className.$methodName")
        builder.append("(")
        val parameterValues: Array<Any?> = joinPoint.args
        for (i in parameterValues.indices) {
            val arg: Any? = parameterValues[i]
            if (i == 0) {
                builder.append(arg)
            } else {
                builder.append(", ")
                    .append(arg)
            }
        }
        builder.append(")")
        val tag: String = builder.toString()
        val currentTimeMillis: Long = System.currentTimeMillis()
        if (currentTimeMillis - lastTime < singleClick.value && (tag == lastTag)) {
            Timber.tag("SingleClick")
            Timber.i("A quick click occurred within %s milliseconds: %s", singleClick.value, tag)
            return
        }
        lastTime = currentTimeMillis
        lastTag = tag
        //Execute original method
        joinPoint.proceed()
    }
}