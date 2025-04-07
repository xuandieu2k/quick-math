package com.dhug.example.helper

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dhug.example.R
import com.dhug.example.presentation.view.activity.HomeActivity
import com.dhug.example.utils.AppConstants
import javax.inject.Inject

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 12 / 03 / 2025
 */
class NotificationHelper @Inject constructor(
    private val context: Context
) {
    fun showNotification(ticketCount: Int) {
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
        val parentStack = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        val resultPendingIntent = createPendingIntent(parentStack)

        val notification = NotificationCompat.Builder(context, AppConstants.CHANEL_ID_NOTIFICATION_REMINDER)
//            .setSmallIcon(R.drawable.logo_driver_note)
//            .setContentTitle("Reminder")
            .setContentText("You have $ticketCount ticket to process today!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .build()
        notificationManager?.notify(2001, notification)
    }

    private fun createPendingIntent(parentStack: TaskStackBuilder): PendingIntent {
        val randomID = System.currentTimeMillis().toInt()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            parentStack.getPendingIntent(
                randomID, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            parentStack.getPendingIntent(randomID, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

}