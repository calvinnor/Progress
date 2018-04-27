package com.calvinnor.progress.service

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.injection.dependencyComponent
import com.calvinnor.progress.model.TaskModel
import javax.inject.Inject

/**
 * Service invoked for Alarms via AlarmManager.
 */
class AlarmService : IntentService(TAG) {

    companion object {
        private const val TAG = "AlarmService"

        private const val ACTION_SHOW_NOTIFICATION = "show_notification"
        private const val TASK_ID = "notification_task_id"

        private const val WAKELOCK_TIMEOUT = 60 * 1000L // 1 minute

        /**
         * Build an intent to trigger the AlarmService.
         */
        fun buildIntent(context: Context, taskModel: TaskModel): PendingIntent {
            val alarmIntent = Intent(context, AlarmService::class.java)
            alarmIntent.apply {
                action = AlarmService.ACTION_SHOW_NOTIFICATION
                putExtra(TASK_ID, taskModel.id)
            }
            return PendingIntent.getService(context, 0, alarmIntent, 0)
        }
    }

    @Inject
    lateinit var dataProxy: DataProxy

    @Inject
    lateinit var powerManager: PowerManager

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        dependencyComponent.inject(this)

        val partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)
        partialWakeLock.acquire(WAKELOCK_TIMEOUT)

        val taskId = intent.getStringExtra(TASK_ID)
        val taskModel = dataProxy.getTask(taskId)

        // Post a notification now
        taskModel?.let { SystemServices.notifications.showNotification(it) }

        partialWakeLock.release()
    }
}
