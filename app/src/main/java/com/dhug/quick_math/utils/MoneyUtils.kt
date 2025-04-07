package com.dhug.quick_math.utils

import android.annotation.SuppressLint
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Singleton

@Singleton
object MoneyUtils {

    fun formatBigDecimal(
        value: BigDecimal,
        locale: Locale = Locale.getDefault(),
        asCurrency: Boolean = false
    ): String {
        val formatter = if (asCurrency) {
            NumberFormat.getCurrencyInstance(locale)
        } else {
            NumberFormat.getNumberInstance(locale)
        }
        return formatter.format(value)
    }

    @SuppressLint("DefaultLocale")
    fun formatCurrency(amount: Double): String {
        return if (amount % 1 == 0.0) {
            String.format("%,.0f", amount)
        } else {
            String.format("%,.2f", amount)
        }
    }

    fun getCodeCurrency(formatPrice: String): String {
        return try {
            formatPrice.substring(0, 1)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun formatNumber(value: BigDecimal): String {
        val thousand = BigDecimal(1_000)
        val million = BigDecimal(1_000_000)
        val billion = BigDecimal(1_000_000_000)

        val numberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }

        return when {
            value >= billion -> "${numberFormat.format(value.divide(billion, 2, RoundingMode.HALF_UP))}B"
            value >= million -> "${numberFormat.format(value.divide(million, 2, RoundingMode.HALF_UP))}M"
            value >= thousand -> "${numberFormat.format(value.divide(thousand, 2, RoundingMode.HALF_UP))}K"
            else -> numberFormat.format(value)
        }
    }

    fun getLocaleFromCurrency(currencyCode: String): Locale {
        return when (currencyCode.uppercase(Locale.US)) {
            "USD" -> Locale.US       // Mỹ - Đô la Mỹ
            "VND" -> Locale("vi", "VN") // Việt Nam - Đồng Việt Nam
            "EUR" -> Locale.GERMANY  // Đức - Euro
            "JPY" -> Locale.JAPAN    // Nhật - Yên Nhật
            "GBP" -> Locale.UK       // Anh - Bảng Anh
            "AUD" -> Locale("en", "AU") // Úc - Đô la Úc
            "CAD" -> Locale.CANADA   // Canada - Đô la Canada
            "CNY" -> Locale.CHINA    // Trung Quốc - Nhân dân tệ
            else -> Locale.getDefault() // Mặc định theo hệ thống
        }
    }

    fun getSymbolCurrency(currencyCode: String): String {
        try {
            val locale = getLocaleFromCurrency(currencyCode)
            val currencyInstance = NumberFormat.getCurrencyInstance(locale)
            currencyInstance.currency = Currency.getInstance(currencyCode)
            return currencyInstance.currency?.symbol ?: "$"
        } catch (ex: Exception) {
            return "$"
        }
    }

}