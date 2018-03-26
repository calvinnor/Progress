package com.calvinnor.progress.app

import android.app.Application
import com.calvinnor.progress.data_layer.TaskDatabase

/**
 * Application class.
 *
 * Initialise all required 3rd party libraries and utility classes here.
 */
class ProgressApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskDatabase.init(this)
    }
}
