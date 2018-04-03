package com.calvinnor.progress.injection

import com.calvinnor.progress.activity.BaseActivity
import com.calvinnor.progress.fragment.BaseFragment
import com.calvinnor.progress.fragment.TaskBottomSheet
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(TasksProvider::class)])
interface TasksComponent {

    fun inject(activity: BaseActivity)
    fun inject(fragment: BaseFragment)
    fun inject(bottomSheet: TaskBottomSheet)
}
