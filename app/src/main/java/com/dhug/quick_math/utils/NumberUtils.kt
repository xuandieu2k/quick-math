package com.dhug.quick_math.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Singleton

@Singleton
object NumberUtils {
    const val DOUBLE_DEFAULT = 0.00

    fun stringToDouble(stri: String?): Double {
        if (stri == null) return DOUBLE_DEFAULT
        try {
            return stri.toDouble()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return DOUBLE_DEFAULT
        }
    }

    fun isValidNumber(str: String?): Boolean {
        str?.let {
            try {
                BigDecimal(str)
                return true
            } catch (ex: Exception) {
                ex.printStackTrace()
                return false
            }
        }
        return false
    }

    fun isValidText(str: String?): Boolean {
        str?.let {
            return it.isNotEmpty()
        }
        return false
    }

    fun textToBigDecimal(str: String?): BigDecimal {
        str?.let {
            try {
                return BigDecimal(str)
            } catch (ex: Exception) {
//                ex.printStackTrace()
                return BigDecimal(0)
            }
        }
        return BigDecimal(0)
    }

    fun BigDecimal.roundToTwoDecimalPlaces(): BigDecimal {
        return this.setScale(2, RoundingMode.HALF_UP)
    }

    fun BigDecimal.roundAndFormat(): String {
        val roundedValue = this.setScale(2, RoundingMode.HALF_UP)
        val formatter = DecimalFormat("#,##0.00")
        return formatter.format(roundedValue)
    }

}