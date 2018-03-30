package com.calvinnor.progress.event

import com.calvinnor.progress.model.TaskModel

/**
 * Class for holding user-initiated events like swipes, edits, creation, etc.
 */
class UserEvents {

    data class TaskAdd(val task: TaskModel)
    data class TaskEdit(val task: TaskModel)

}
