package com.calvinnor.progress.data_layer

import com.calvinnor.progress.event.TaskAddEvent
import com.calvinnor.progress.event.TaskStateChangeEvent
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.util.Events
import org.greenrobot.eventbus.Subscribe

/**
 * A repository class for holding Task information.
 *
 * Clients must use this class instead of {@link TaskDatabase} or it's methods
 */
object TaskRepo {

    private val taskDao = TaskDatabase.taskDao()
    private val dbThread = TaskDatabase.dbThread

    private val allTasksList = mutableListOf<TaskModel>()

    init {
        this.allTasksList.addAll(taskDao.getTasks())
        Events.subscribe(this)
    }

    fun getTasks() = allTasksList

    fun insertTask(newTask: TaskModel) {
        allTasksList.add(newTask)
        Events.post(TaskAddEvent(newTask))
        runAsync { taskDao.insert(newTask) }
    }

    fun updateTask(task: TaskModel) {
        allTasksList.find { task.id == it.id }?.updateFromModel(task)
        runAsync { taskDao.updateTask(task) }
    }

    fun setComplete(task: TaskModel, isComplete: Boolean) {
        allTasksList.find { task.id == it.id }?.isComplete = isComplete
        Events.post(TaskStateChangeEvent(task))
        runAsync { taskDao.updateTask(task) }
    }

    fun deleteTask(taskModel: TaskModel) {
        allTasksList.remove(allTasksList.find { taskModel.id == it.id })
        runAsync { taskDao.deleteTask(taskModel) }
    }

    private fun runAsync(task: () -> Any) {
        dbThread.post(Runnable { task() })
    }

    @Subscribe
    fun onTaskStateChanged(taskStateChangeEvent: TaskStateChangeEvent) {
        runAsync { taskDao.updateTask(taskStateChangeEvent.task) }
    }
}
