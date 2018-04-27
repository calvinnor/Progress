@file: JvmName("AlarmScheduler")

package com.calvinnor.progress.service

import android.app.AlarmManager
import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.AlarmManagerCompat
import com.calvinnor.progress.injection.dependencyComponent
import com.calvinnor.progress.model.TaskModel
import java.util.*
import javax.inject.Inject

/**
 * Wrapper class for scheduling Alarms.
 */
class AlarmScheduler(context: Context) : ContextWrapper(context) {

    @Inject
    lateinit var alarmManager: AlarmManager

    init {
        dependencyComponent.inject(this)
    }

    /**
     * Schedule an alarm for the given Task.
     */
    fun scheduleAlarm(taskModel: TaskModel) {
        // Don't schedule an alarm for a task which is in the past
        if (taskModel.dateTime <= Calendar.getInstance()) return

        AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                taskModel.dateTime.timeInMillis,
                AlarmService.buildIntent(baseContext, taskModel)
        )
    }
}
