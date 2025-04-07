package com.dhug.example.utils

import android.content.Context
import android.text.format.DateUtils
import com.dhug.example.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Singleton
object TimeUtils {
    const val FORMAT_SERVER = "yyyy-MM-dd HH:mm:ss"
    private const val FORMAT_CLIENT = "HH:mm dd-MM-yyyy"
    const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
    private const val TIME_MINUS_FORMAT = "HH:mm"
    private const val TIME_MINUS_FORMAT_MORE = "hh:mm a"
    private const val DAY_MONTH_YEAR_FORMAT = "dd/MM/yyyy"

    private const val FORMAT_DD_MMMM_YY = "dd-MMM-yy"
    private const val FORMAT_MMMM_YYYY = "MMMM, yyyy"
    private const val FORMAT_FILTER = "yyyy-MM"

    fun Long.toDateText(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat(DAY_MONTH_YEAR_FORMAT, Locale.getDefault())
        return format.format(dateTime)
    }

    fun Long.toMonthYearFilter(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat(FORMAT_FILTER, Locale.getDefault())
        return format.format(dateTime)
    }

    fun Long.toFullDateTimeText(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return format.format(dateTime)
    }

    fun Long.toTimeText(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat(TIME_MINUS_FORMAT_MORE, Locale.getDefault())
        return format.format(dateTime)
    }

    fun Long.toFormatMonthAndYear(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat(FORMAT_MMMM_YYYY, Locale.getDefault())
        return format.format(dateTime)
    }

    fun Long.getPairDateTime(): Pair<Int, Int> {
        val dateTime = Date(this)
        @Suppress("DEPRECATION")
        return Pair(dateTime.hours, dateTime.minutes)
    }

    fun getRemainingDaysText(targetDateMillis: Long): String {
        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)

        val targetCalendar = Calendar.getInstance()
        targetCalendar.timeInMillis = targetDateMillis
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0)
        targetCalendar.set(Calendar.MINUTE, 0)
        targetCalendar.set(Calendar.SECOND, 0)
        targetCalendar.set(Calendar.MILLISECOND, 0)

        val diffInMillis = targetCalendar.timeInMillis - todayCalendar.timeInMillis
        val daysDifference = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

        return when {
            daysDifference > 0 -> "$daysDifference days left"
            daysDifference == 0 -> "Today"
            else -> "Expired"
        }
    }

    fun getTypeRemainingDaysText(targetDateMillis: Long): Int {
        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)

        val targetCalendar = Calendar.getInstance()
        targetCalendar.timeInMillis = targetDateMillis
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0)
        targetCalendar.set(Calendar.MINUTE, 0)
        targetCalendar.set(Calendar.SECOND, 0)
        targetCalendar.set(Calendar.MILLISECOND, 0)

        val diffInMillis = targetCalendar.timeInMillis - todayCalendar.timeInMillis
        val daysDifference = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

        return when {
            daysDifference > 0 -> 1
            daysDifference == 0 -> 0
            else -> -1
        }
    }

    fun formatDuration(seconds: Long): String = if (seconds < 60) {
        seconds.toString()
    } else {
        DateUtils.formatElapsedTime(seconds)
    }

    fun formatLongToDateTime(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    fun Long.toFormatData(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun isSameDay(date1: Calendar, date2: Calendar): Boolean {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
    }

    fun Long.toCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = this@toCalendar
        }
    }

    fun initDataTime(miliSeconds: Long, hour: Int, minutes: Int): Long {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = miliSeconds
            this[Calendar.MINUTE] = minutes
//            this[Calendar.HOUR] = hour
            set(Calendar.HOUR_OF_DAY, hour)
        }
        return calendar.timeInMillis
    }

    fun formatDate(timestamp: Long): Pair<String, String> {
        val date = Date(timestamp)
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

        val day = dayFormat.format(date)
        val month = monthFormat.format(date)

        return Pair(day, month)
    }

    fun getStartAndEndOfDay(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    fun getStartAndEndOfDay(timeInMillis: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    fun getStartAndEndOfThisWeek(timeInMillis: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        // Đặt về đầu tuần (Thứ 2)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis

        // Đặt về cuối tuần (Chủ Nhật)
        calendar.add(Calendar.DAY_OF_YEAR, 6) // Sử dụng DAY_OF_YEAR thay vì DAY_OF_WEEK
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfWeek = calendar.timeInMillis

        return Pair(startOfWeek, endOfWeek)
    }

    fun getStartAndEndOfThisMonth(timeInMillis: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        // Đặt về ngày đầu tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        // Đặt về ngày cuối tháng
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 0) // Lùi lại 1 ngày để lấy ngày cuối tháng trước
        calendar.set(Calendar.HOUR_OF_DAY, 12) // 12h trưa
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    fun getStartAndEndOfThisYear(timeInMillis: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        // Đặt về đầu năm
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfYear = calendar.timeInMillis

        // Đặt về cuối năm
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 12) // 12h trưa
        val endOfYear = calendar.timeInMillis

        return Pair(startOfYear, endOfYear)
    }


    fun formatDateMonthText(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat(FORMAT_DD_MMMM_YY, Locale.ENGLISH)
        return sdf.format(date)
    }


    fun daysBetween(time1: Long, time2: Long): Int {
        val calendar1 = Calendar.getInstance().apply {
            timeInMillis = time1
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val calendar2 = Calendar.getInstance().apply {
            timeInMillis = time2
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        val diffInMillis = calendar2.timeInMillis - calendar1.timeInMillis
        return if (TimeUnit.MILLISECONDS.toDays(diffInMillis)
                .toInt() < 0
        ) 1 else (TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt() + 1) // plus more one
    }

}