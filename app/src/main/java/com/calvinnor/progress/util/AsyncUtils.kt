@file:JvmName("AsyncUtils")

package com.calvinnor.progress.util

import android.os.AsyncTask

class async(private val asyncJob: () -> Any) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        asyncJob()
        return null
    }
}
