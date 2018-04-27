package com.calvinnor.progress.injection

import com.calvinnor.progress.activity.BaseActivity
import com.calvinnor.progress.fragment.BaseFragment
import com.calvinnor.progress.fragment.TaskBottomSheet
import com.calvinnor.progress.service.AlarmScheduler
import com.calvinnor.progress.service.AlarmService
import com.calvinnor.progress.service.Notifications
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DependencyProvider::class)])
interface DependencyComponent {

    fun inject(activity: BaseActivity)
    fun inject(fragment: BaseFragment)
    fun inject(bottomSheet: TaskBottomSheet)
    fun inject(alarmScheduler: AlarmScheduler)
    fun inject(alarmService: AlarmService)
    fun inject(notifications: Notifications)
}
