package com.calvinnor.progress.data_layer

import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.event.TaskStateChangeEvent
import com.calvinnor.progress.event.TaskUpdateEvent
import com.calvinnor.progress.event.TasksLoadedEvent
import com.calvinnor.progress.event.UserEvents
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.util.Events

/**
 * A repository class for holding Task information.
 *
 * Clients must never use this class directly.
 * Instead, communicate via the Proxy interface.
 */
object TaskRepo : DataProxy {

    private val taskDao = TaskDatabase.taskDao()
    private val dbThread = TaskDatabase.dbThread

    private val allTasksList = mutableListOf<TaskModel>()

    fun initialise() {
        allTasksList.addAll(taskDao.getTasks())
        Events.postSticky(TasksLoadedEvent(allTasksList))
    }

    override fun getTasks() = allTasksList

    override fun getTask(taskId: String) = allTasksList.find { taskId.equals(it.id) }

    override fun insertTask(newTask: TaskModel) {
        allTasksList.add(newTask)
        Events.post(UserEvents.TaskAdd(newTask))
        runAsync { taskDao.insert(newTask) }
    }

    override fun updateTask(task: TaskModel) {
        val taskToUpdate = allTasksList.find { task.id == it.id }
        if (taskToUpdate == null) return
        taskToUpdate.updateFromModel(task)
        Events.post(TaskUpdateEvent(taskToUpdate))
        runAsync { taskDao.updateTask(taskToUpdate) }
    }

    override fun setComplete(task: TaskModel, isComplete: Boolean) {
        allTasksList.find { task.id == it.id }?.isComplete = isComplete
        Events.post(TaskStateChangeEvent(task))
        runAsync { taskDao.updateTask(task) }
    }

    override fun deleteTask(taskModel: TaskModel) {
        allTasksList.remove(allTasksList.find { taskModel.id == it.id })
        runAsync { taskDao.deleteTask(taskModel) }
    }

    private fun runAsync(task: () -> Any) {
        dbThread.post(Runnable { task() })
    }
}
