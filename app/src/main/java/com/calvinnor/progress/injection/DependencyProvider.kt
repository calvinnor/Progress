package com.calvinnor.progress.injection

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import com.calvinnor.progress.app.ProgressApp
import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.data_layer.TaskRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DependencyProvider {

    @Provides
    @Singleton
    fun dataProxy(): DataProxy = TaskRepo

    @Provides
    @Singleton
    fun appContext() = ProgressApp.appContext

    @Provides
    @Singleton
    fun notificationManager() = getService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun alarmManager() = getService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @Singleton
    fun powerManager() = getService(Context.POWER_SERVICE) as PowerManager

    private fun getService(serviceName: String) = appContext().getSystemService(serviceName)
}
