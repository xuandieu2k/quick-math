package com.dhug.example.framework.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dhug.example.framework.worker.TicketWorker

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 12 / 03 / 2025
 */
class TicketBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val workManager = WorkManager.getInstance(it)
            val workRequest = OneTimeWorkRequestBuilder<TicketWorker>().build()
            workManager.enqueue(workRequest)
        }
    }
}

