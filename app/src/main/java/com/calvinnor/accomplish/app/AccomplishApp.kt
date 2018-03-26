package com.calvinnor.accomplish.app

import android.app.Application
import com.calvinnor.accomplish.data_layer.TaskDatabase

/**
 * Application class.
 *
 * Initialise all required 3rd party libraries and utility classes here.
 */
class AccomplishApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskDatabase.init(this)
    }
}
