package com.calvinnor.progress.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.calvinnor.progress.data_layer.TASK_COL_DESCRIPTION
import com.calvinnor.progress.data_layer.TASK_COL_ID
import com.calvinnor.progress.data_layer.TASK_COL_TITLE
import com.calvinnor.progress.data_layer.TASK_TABLE_NAME
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import com.calvinnor.progress.model.TaskState.Companion.INBOX
import java.util.*

@Entity(tableName = TASK_TABLE_NAME)
data class TaskModel(
        @PrimaryKey @ColumnInfo(name = TASK_COL_ID) var id: String,
        @ColumnInfo(name = TASK_COL_TITLE) var title: String,
        @ColumnInfo(name = TASK_COL_DESCRIPTION) var description: String,
        @Embedded var state: TaskState,
        @Embedded var priority: TaskPriority) {

    constructor() : this("null", "null", "null", TaskState(INBOX), TaskPriority(P3))

    companion object {
        fun buildFrom(taskTitle: String, taskDescription: String,
                      taskState: TaskState, taskPriority: TaskPriority): TaskModel {
            return TaskModel(UUID.randomUUID().toString(), taskTitle, taskDescription, taskState, taskPriority)
        }
    }

    fun updateFromModel(taskModel: TaskModel) {
        this.title = taskModel.title
        this.description = taskModel.description
        this.state = taskModel.state
        this.priority = taskModel.priority
    }
}
