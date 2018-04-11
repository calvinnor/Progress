package com.calvinnor.progress.contract

import com.calvinnor.progress.model.TaskModel

/**
 * Abstraction layer for clients to communicate with the Task data source.
 *
 * Always use this interface for communication, and not the classes directly that implement it.
 */
interface DataProxy {

    /**
     * Get the list of all tasks.
     */
    fun getTasks(): MutableList<TaskModel>

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
