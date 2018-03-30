package com.calvinnor.progress.model

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import com.calvinnor.progress.R

data class TaskPriority(val priority: Int) {

    companion object {
        const val P1 = 1000
        const val P2 = 2000
        const val P3 = 3000
    }
}

@ColorInt
fun TaskPriority.getColor(context: Context): Int {
    val colorRes = when (priority) {
        TaskPriority.P1 -> R.color.task_priority_p1
        TaskPriority.P2 -> R.color.task_priority_p2
        TaskPriority.P3 -> R.color.task_priority_p3
        else -> R.color.task_priority_p3
    }
    return ContextCompat.getColor(context, colorRes)
}
