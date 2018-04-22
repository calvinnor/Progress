package com.calvinnor.progress.model.tree

import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskState

/**
 * A node for storing tasks by State.
 *
 * This is one of many classifications for a task.
 */
class StateNode(var taskState: TaskState) {

    var taskModels: MutableList<TaskModel> = mutableListOf()

}
