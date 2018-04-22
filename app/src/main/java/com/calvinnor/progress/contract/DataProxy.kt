package com.calvinnor.progress.contract

import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskState

/**
 * Abstraction layer for clients to communicate with the Task data source.
 *
 * Always use this interface for communication, and not the classes directly that implement it.
 */
interface DataProxy {

    /**
     * Get the list of all tasks.
     */
    fun getAllTasks(): MutableList<TaskModel>

    /**
     * Get the list of tasks by a State i.e. Inbox, Pending, Done
     */
    fun getTasksForState(taskState: TaskState): MutableList<TaskModel>

    /**
     * Get a specific task by it's ID.
     */
    fun getTask(taskId: String): TaskModel?

    /**
     * Add a task to the repository.
     */
    fun insertTask(newTask: TaskModel)

    /**
     * Updates a task in the repository.
     */
    fun updateTask(task: TaskModel)

    /**
     * Deletes the given task from repository.
     */
    fun deleteTask(taskModel: TaskModel)

}
