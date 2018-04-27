package com.calvinnor.progress.service

import android.annotation.SuppressLint
import com.calvinnor.progress.app.ProgressApp

/**
 * Wrapper class for getting a hold of utility classes, which internally use
 * Android's System Services.
 */
@SuppressLint("StaticFieldLeak")
object SystemServices {

    val notifications = Notifications(context = ProgressApp.appContext)
    val alarmScheduler = AlarmScheduler(context = ProgressApp.appContext)

}
