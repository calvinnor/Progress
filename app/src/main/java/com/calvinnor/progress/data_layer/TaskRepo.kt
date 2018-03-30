package com.calvinnor.progress.data_layer

import com.calvinnor.progress.event.TaskStateChangeEvent
import com.calvinnor.progress.event.TaskUpdateEvent
import com.calvinnor.progress.event.UserEvents
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

    fun getTask(taskId: String) = allTasksList.find { taskId.equals(it.id) }

    fun insertTask(newTask: TaskModel) {
        allTasksList.add(newTask)
        Events.post(UserEvents.TaskAdd(newTask))
        runAsync { taskDao.insert(newTask) }
    }

    fun updateTask(task: TaskModel) {
        val taskToUpdate = allTasksList.find { task.id == it.id }
        if (taskToUpdate == null) return
        taskToUpdate.updateFromModel(task)
        Events.post(TaskUpdateEvent(taskToUpdate))
        runAsync { taskDao.updateTask(taskToUpdate) }
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
