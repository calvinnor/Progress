package com.calvinnor.accomplish.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.calvinnor.accomplish.data_layer.TASK_COL_COMPLETE
import com.calvinnor.accomplish.data_layer.TASK_COL_TITLE
import com.calvinnor.accomplish.data_layer.TASK_TABLE_NAME

@Entity(tableName = TASK_TABLE_NAME)
data class TaskModel(
        @ColumnInfo(name = TASK_COL_TITLE) var title: String,
        @ColumnInfo(name = TASK_COL_COMPLETE) var isComplete: Boolean) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor() : this("null", false)
}
