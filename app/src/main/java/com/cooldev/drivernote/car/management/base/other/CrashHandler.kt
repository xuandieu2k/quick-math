package com.dhug.example.base.other

import android.app.*
import android.content.*
import android.os.Process
import com.dhug.example.base.ui.activity.RestartActivity

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class CrashHandler private constructor(private val application: Application) :
    Thread.UncaughtExceptionHandler {

    companion object {

        /** Crash file name */
        private const val CRASH_FILE_NAME: String = "crash_file"

        /** Crash time record */
        private const val KEY_CRASH_TIME: String = "key_crash_time"

        /**
         * Register Crash monitoring
         */
        fun register(application: Application) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(application))
        }
    }

    private val nextHandler: Thread.UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()

    init {
        if ((javaClass.name == nextHandler?.javaClass?.name)) {
            // Please do not register Crash listener repeatedly
            throw IllegalStateException("are you ok?")
        }
    }

    @Suppress("ApplySharedPref")
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val sharedPreferences: SharedPreferences = application.getSharedPreferences(
            CRASH_FILE_NAME, Context.MODE_PRIVATE
        )
        val currentCrashTime: Long = System.currentTimeMillis()
        val lastCrashTime: Long = sharedPreferences.getLong(KEY_CRASH_TIME, 0)
        // Record the time of the current crash for comparison the next time it crashes
        sharedPreferences.edit().putLong(KEY_CRASH_TIME, currentCrashTime).commit()

        // Fatal exception mark: If the time of the last crash is less than 5 minutes from the current crash, it is determined to be a fatal exception.
        val deadlyCrash: Boolean = currentCrashTime - lastCrashTime < 1000 * 60 * 5
//        if (AppConfig.isDebug()) {
//            CrashActivity.start(application, throwable)
//        } else {
//            if (!deadlyCrash) {
//                // Automatically restart the application if it is not a fatal exception
//                RestartActivity.start(application)
//            }
//        }
        if (!deadlyCrash) {
            // Automatically restart the application if it is not a fatal exception
            RestartActivity.start(application)
        }

        //Do not trigger system crash handling (com.android.internal.os.RuntimeInit$KillApplicationHandler)
        if (nextHandler != null && !nextHandler.javaClass.name
                .startsWith("com.android.internal.os")
        ) {
            nextHandler.uncaughtException(thread, throwable)
        }

        // Kill the process (this should be done by the system, but it will pop up an extra crash dialog box, so we need to kill the process manually)
        Process.killProcess(Process.myPid())
        System.exit(10)
    }
}