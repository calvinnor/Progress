package com.calvinnor.progress.data_layer

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.calvinnor.progress.model.TaskModel

/**
 * Interface to manipulate the database.
 * Make sure to add the corresponding Runnables for each method to {@link TaskHandler} for easy access.
 */
@Dao
interface TaskDao {

    @Query("SELECT * from ${TASK_TABLE_NAME}")
    fun getTasks(): MutableList<TaskModel>

    @Insert(onConflict = REPLACE)
    fun insert(taskModel: TaskModel)

    @Update
    fun updateTask(taskModel: TaskModel)

    @Delete
    fun deleteTasks(vararg taskModels: TaskModel)

    @Delete
    fun deleteTask(taskModel: TaskModel)

}
