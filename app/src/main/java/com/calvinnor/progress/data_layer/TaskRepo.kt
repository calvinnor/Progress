package com.calvinnor.progress.data_layer

import android.os.Handler
import com.calvinnor.progress.event.AddTaskEvent
import com.calvinnor.progress.event.TasksEvent
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.util.EventBus

/**
 * A repository class for holding Task information.
 *
 * Clients must use this class instead of {@link TaskDatabase} or it's methods
 */
object TaskRepo {

    private val taskDao = TaskDatabase.taskDao()
    private val dbThread = TaskDatabase.dbThread
    private val uiThread = Handler()

    private var taskList: MutableList<TaskModel> = mutableListOf()

    fun getTasks(force: Boolean) {
        if (!force) {
            EventBus.post(TasksEvent(taskList))
            return
        }
        getTasks()
    }

    fun getTasks() {
        dbThread.post(Runnable {
            val dbTaskList = taskDao.getTasks()
            this.taskList = dbTaskList // Cache these locally
            EventBus.post(TasksEvent(taskList))
        })
    }

    fun insertTask(newTask: TaskModel) = dbThread.post(Runnable {
        taskList.add(newTask)
        taskDao.insert(newTask)
        EventBus.post(AddTaskEvent(newTask))
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
}
