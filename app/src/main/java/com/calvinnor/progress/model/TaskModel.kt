package com.calvinnor.progress.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.calvinnor.progress.data_layer.TASK_COL_COMPLETE
import com.calvinnor.progress.data_layer.TASK_COL_ID
import com.calvinnor.progress.data_layer.TASK_COL_TITLE
import com.calvinnor.progress.data_layer.TASK_TABLE_NAME
import java.util.*

@Entity(tableName = TASK_TABLE_NAME)
data class TaskModel(
        @PrimaryKey @ColumnInfo(name = TASK_COL_ID) var id: String,
        @ColumnInfo(name = TASK_COL_TITLE) var title: String,
        @ColumnInfo(name = TASK_COL_COMPLETE) var isComplete: Boolean) {

    constructor() : this("null", "null", false)

    companion object {
        fun buildFrom(taskTitle: String, isComplete: Boolean): TaskModel {
            return TaskModel(UUID.randomUUID().toString(), taskTitle, isComplete)
        }
    }

    fun updateFromModel(taskModel: TaskModel) {
        this.title = taskModel.title
        this.isComplete = taskModel.isComplete
    }
}
