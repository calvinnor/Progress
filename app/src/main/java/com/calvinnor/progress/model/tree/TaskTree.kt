package com.calvinnor.progress.model.tree

import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskState

/**
 * An in-memory data holder for our tasks.
 *
 * Separation by Task State is available for faster lookups.
 * In future, we can have multiple trees of classification.
 */
object TaskTree {

    private val allTasksList = mutableListOf<TaskModel>()

    // Root of the tree
    private val tasksRoot = mutableListOf<StateNode>()

    // Task State Nodes
    private val inboxNode = StateNode(TaskState(TaskState.INBOX))
    private val pendingNode = StateNode(TaskState(TaskState.PENDING))
    private val doneNode = StateNode(TaskState(TaskState.DONE))

    init {
        tasksRoot.addAll(arrayOf(inboxNode, pendingNode, doneNode))
    }

    /**
     * Set the list of tasks.
     */
    fun setTasks(listOfTasks: MutableList<TaskModel>) {
        allTasksList.clear()
        allTasksList.addAll(listOfTasks)

        setTasks(inboxNode)
        setTasks(pendingNode)
        setTasks(doneNode)
    }

    /**
     * Get all tasks available.
     */
    fun getAllTasks() = allTasksList

    /**
     * Get all tasks for a particular state.
     */
    fun getTasksForState(taskState: TaskState) = tasksRoot.find { it.taskState == taskState }?.taskModels

    /**
     * Get the task by a given ID.
     */
    fun getTaskById(taskId: String) = allTasksList.find { it.id == taskId }

    /**
     * Insert a task into the tree.
     */
    fun insertTask(newTask: TaskModel) {
        allTasksList.add(newTask)
        tasksRoot.find { it.taskState == newTask.state }?.taskModels?.add(newTask)
    }

    /**
     * Update a given task.
     */
    fun updateTask(updatedTask: TaskModel) {
        val taskToUpdate = getTaskById(updatedTask.id) ?: return
        val oldState = taskToUpdate.state
        val newState = updatedTask.state

        taskToUpdate.updateFromModel(updatedTask)
        if (oldState != newState) {
            tasksRoot.find { it.taskState == oldState }?.taskModels?.remove(taskToUpdate)
            tasksRoot.find { it.taskState == newState }?.taskModels?.add(taskToUpdate)
        }
    }

    /**
     * Removes a task from the tree.
     */
    fun removeTask(removeTask: TaskModel) {
        allTasksList.remove(getTaskById(removeTask.id))
        tasksRoot.find { it.taskState == removeTask.state }?.taskModels?.remove(removeTask)
    }

    private fun setTasks(stateNode: StateNode) {
        stateNode.taskModels.clear()
        stateNode.taskModels.addAll(allTasksList.filter { it.state == stateNode.taskState })
    }
}
