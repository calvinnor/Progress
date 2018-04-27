package com.calvinnor.progress.app

import android.app.Application
import android.content.Context
import com.calvinnor.progress.data_layer.TaskDatabase
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.injection.initialise

/**
 * Application class.
 *
 * Initialise all required 3rd party libraries and utility classes here.
 */
class ProgressApp : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initDatabase()
        initDagger()
    }

    private fun initDatabase() {
        TaskDatabase.init(this)
        TaskRepo.initialise()
    }

    private fun initDagger() {
        initialise()
    }
}
