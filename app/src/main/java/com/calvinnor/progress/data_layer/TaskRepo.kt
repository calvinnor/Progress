package com.calvinnor.progress.data_layer

import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.event.TaskUpdateEvent
import com.calvinnor.progress.event.TasksLoadedEvent
import com.calvinnor.progress.event.UserEvents
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.model.tree.TaskTree
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

    // In-memory cache of Tasks
    private val taskTree = TaskTree

    // Prevent multiple initialisation
    private var isInitialised = false

    fun initialise() {
        if (isInitialised) throw RuntimeException("Do not call init more than once!")
        runAsync {
            taskTree.setTasks(taskDao.getTasks())
            isInitialised = true
            Events.postSticky(TasksLoadedEvent())
        }
    }

    override fun getAllTasks() = taskTree.getAllTasks()

    override fun getTasksForState(taskState: TaskState) = taskTree.getTasksForState(taskState)!!

    override fun getTask(taskId: String): TaskModel {
        val task = taskTree.getTaskById(taskId)
        return if (task == null) taskDao.getTask(taskId) else task
    }

    override fun insertTask(newTask: TaskModel) {
        taskTree.insertTask(newTask)
        Events.post(UserEvents.TaskAdd(newTask))
        runAsync { taskDao.insert(newTask) }
    }

    override fun updateTask(task: TaskModel) {
        taskTree.updateTask(task)
        Events.post(TaskUpdateEvent(task))
        runAsync { taskDao.updateTask(task) }
    }

    override fun deleteTask(taskModel: TaskModel) {
        taskTree.removeTask(taskModel)
        runAsync { taskDao.deleteTask(taskModel) }
    }

    private fun runAsync(task: () -> Any) {
        dbThread.post(Runnable { task() })
    }
}
