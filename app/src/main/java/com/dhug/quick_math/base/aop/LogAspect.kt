package com.dhug.quick_math.base.aop

import android.os.Looper
import android.os.Trace
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import org.aspectj.lang.reflect.MethodSignature
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22/10/2022
 */
@Suppress("unused")
@Aspect
class LogAspect {

    /**
     *Construction method entry point
     */
    @Pointcut("execution(@vn.cooldev.dashcam.base.aop.Log *.new(..))")
    fun constructor() {}

    /**
     *Method entry point
     */
    @Pointcut("execution(@vn.cooldev.dashcam.base.aop.Log * *(..))")
    fun method() {}

    /**
     * Method substitution at connection point
     */
    @Around("(method() || constructor()) && @annotation(log)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, log: Log): Any? {
        enterMethod(joinPoint, log)
        val startNanos: Long = System.nanoTime()
        val result: Any? = joinPoint.proceed()
        val stopNanos: Long = System.nanoTime()
        exitMethod(joinPoint, log, result, TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos))
        return result
    }

    /**
     * Cut in before method execution
     */
    private fun enterMethod(joinPoint: ProceedingJoinPoint, log: Log) {
        val codeSignature: CodeSignature = joinPoint.signature as CodeSignature

        //The class where the method is located
        val className: String = codeSignature.declaringType.name
        // method name
        val methodName: String = codeSignature.name
        // Collection of method parameter names
        val parameterNames: Array<String?> = codeSignature.parameterNames
        //Method parameter value collection
        val parameterValues: Array<Any?> = joinPoint.args

        //Record and print method information
        val builder: StringBuilder =
            getMethodLogInfo(className, methodName, parameterNames, parameterValues)
        log(log.value, builder.toString())
        val section: String = builder.substring(2)
        Trace.beginSection(section)
    }

    /**
     * Get the log information of the method
     *
     * @param className class name
     * @param methodName method name
     * @param parameterNames method parameter name collection
     * @param parameterValues method parameter value collection
     */
    private fun getMethodLogInfo(className: String, methodName: String, parameterNames: Array<String?>, parameterValues: Array<Any?>): StringBuilder {
        val builder: StringBuilder = StringBuilder("\u21E2 ")
        builder.append(className)
            .append(".")
            .append(methodName)
            .append('(')
        for (i in parameterValues.indices) {
            if (i > 0) {
                builder.append(", ")
            }
            builder.append(parameterNames[i]).append('=')
            builder.append(parameterValues[i].toString())
        }
        builder.append(')')
        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().name).append("\"]")
        }
        return builder
    }

    /**
     * After the method is executed, cut out
     *
     * @param result The result after method execution
     * @param lengthMillis The time required to execute the method
     */
    private fun exitMethod(joinPoint: ProceedingJoinPoint, log: Log, result: Any?, lengthMillis: Long) {
        Trace.endSection()
        val signature: Signature = joinPoint.signature
        val className: String? = signature.declaringType.name
        val methodName: String? = signature.name
        val builder: StringBuilder = StringBuilder("\u21E0 ")
            .append(className)
            .append(".")
            .append(methodName)
            .append(" [")
            .append(lengthMillis)
            .append("ms]")

        // Determine whether the method has a return value
        if (signature is MethodSignature && signature.returnType != Void.TYPE) {
            builder.append(" = ")
            builder.append(result.toString())
        }
        log(log.value, builder.toString())
    }

    private fun log(tag: String?, msg: String?) {
        Timber.tag(tag?:"Empty")
        Timber.d(msg)
    }
}