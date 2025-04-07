package com.dhug.example.utils

import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 15 / 03 / 2025
 */
@Singleton
object StringUtils {

    fun String.capitalizeFirstLetter(): String {
        if (this.isEmpty()) return this
        return this[0].uppercase() + this.substring(1)
    }
}