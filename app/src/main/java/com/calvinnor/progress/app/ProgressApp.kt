package com.calvinnor.progress.app

import android.app.Application
import com.calvinnor.progress.data_layer.TaskDatabase
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.injection.DaggerTasksComponent
import com.calvinnor.progress.injection.TasksProvider

/**
 * Application class.
 *
 * Initialise all required 3rd party libraries and utility classes here.
 */
class ProgressApp : Application() {

    companion object {
        val tasksComponent = buildTasksComponent()

        private fun buildTasksComponent() = DaggerTasksComponent
                .builder()
                .tasksProvider(TasksProvider)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        initDatabase()
    }

    private fun initDatabase() {
        TaskDatabase.init(this)
        TaskRepo.initialise()
    }
}
