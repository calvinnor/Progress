package com.calvinnor.progress.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.calvinnor.progress.data_layer.*
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import java.util.*

@Entity(tableName = TASK_TABLE_NAME)
data class TaskModel(
        @PrimaryKey @ColumnInfo(name = TASK_COL_ID) var id: String,
        @ColumnInfo(name = TASK_COL_TITLE) var title: String,
        @ColumnInfo(name = TASK_COL_COMPLETE) var isComplete: Boolean,
        @Embedded var priority: TaskPriority) {

    constructor() : this("null", "null", false, TaskPriority(P3))

    companion object {
        fun buildFrom(taskTitle: String, isComplete: Boolean, taskPriority: TaskPriority): TaskModel {
            return TaskModel(UUID.randomUUID().toString(), taskTitle, isComplete, taskPriority)
        }
    }

    fun updateFromModel(taskModel: TaskModel) {
        this.title = taskModel.title
        this.isComplete = taskModel.isComplete
    }
}
