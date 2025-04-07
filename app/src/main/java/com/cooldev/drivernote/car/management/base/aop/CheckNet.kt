package com.dhug.example.base.aop

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
annotation class CheckNet