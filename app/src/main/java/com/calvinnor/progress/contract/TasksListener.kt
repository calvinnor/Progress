package com.calvinnor.progress.contract

import com.calvinnor.progress.model.TaskModel

/**
 * Interface to propagate task click events.
 */
interface TasksListener {

    /**
     * When a task is selected, pass in the model.
     */
    fun onTaskSelected(task: TaskModel)
}