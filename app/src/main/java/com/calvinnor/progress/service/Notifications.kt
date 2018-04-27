package com.calvinnor.progress.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.calvinnor.progress.BuildConfig
import com.calvinnor.progress.R
import com.calvinnor.progress.injection.dependencyComponent
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.util.isOreo
import javax.inject.Inject

/**
 * Wrapper class for showing Notifications to user.
 */
class Notifications(val context: Context) : ContextWrapper(context) {

    companion object {
        private const val DEFAULT_CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.channel"
    }

    @Inject
    lateinit var notificationManager: NotificationManager

    init {
        dependencyComponent.inject(this)
        if (isOreo()) createChannels()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationChannel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                context.getString(R.string.notification_channel_priority),
                NotificationManager.IMPORTANCE_HIGH).apply {

            enableLights(true)
            lightColor = Color.RED
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun showNotification(task: TaskModel) {
        // Build the notification
        val notification = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setContentTitle(task.title)
                .setContentText(task.description)
                .setSmallIcon(R.drawable.ic_progress)
                .build()

        // Issue the notification.
        notificationManager.notify(task.id.hashCode(), notification)
    }
}
