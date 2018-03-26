package com.calvinnor.progress.data_layer

import android.os.Handler
import com.calvinnor.progress.model.TaskModel

/**
 * A repository class for holding Task information.
 *
 * Clients must use this class instead of {@link TaskDatabase} or it's methods
 */
object TaskRepo {

    private var tasksListener: TasksListener? = null

    init {

    }

    fun registerListener(tasksListener: TasksListener) {
        this.tasksListener = tasksListener
    }

    fun unregisterListener(tasksListener: TasksListener) {
        if (this.tasksListener == tasksListener) this.tasksListener = null
    }

    private val taskDao = TaskDatabase.taskDao()
    private val dbThread = TaskDatabase.dbThread
    private val uiThread = Handler()

    fun getTasks() = dbThread.post(Runnable {
        val taskList = taskDao.getTasks()
        tasksListener?.onTasksFetched(taskList)
    })

    fun insertTask(taskModel: TaskModel, callback: Runnable?) = dbThread.post(Runnable {
        taskDao.insert(taskModel)
        postCallToUiThread(callback)
    })

    fun updateTask(taskModel: TaskModel, callback: Runnable?) = dbThread.post(Runnable {
        taskDao.updateTask(taskModel)
        postCallToUiThread(callback)
    })

    fun deleteAllTasks(callback: Runnable?) = dbThread.post(Runnable {
        taskDao.deleteAllTasks()
        postCallToUiThread(callback)
    })

    fun deleteTask(vararg taskModels: TaskModel, callback: Runnable?) = dbThread.post(Runnable {
        taskDao.deleteTasks(*taskModels)
        postCallToUiThread(callback)
    })

    private fun postCallToUiThread(callback: Runnable?) {
        if (callback != null) uiThread.post(callback)
    }

    interface TasksListener {

        fun onTasksFetched(taskList: List<TaskModel>)
    }
}