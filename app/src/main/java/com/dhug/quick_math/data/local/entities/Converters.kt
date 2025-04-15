package com.dhug.quick_math.data.local.entities

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.math.BigDecimal

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    Converters.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 12:05
 * Description: File Converters.kt created by admin - 12/4/25 at 12:05
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun listToString(list: List<String>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}