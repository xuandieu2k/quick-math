package com.dhug.quick_math.framework.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 12 / 03 / 2025
 */

@HiltWorker
class TicketWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
//    private val checkTicketUseCase: CheckTicketUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
//        checkTicketUseCase.execute()
        return Result.success()
    }
}