package com.calvinnor.progress.data_layer

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.calvinnor.progress.app.DATABASE_IN_MEMORY_TEST
import com.calvinnor.progress.model.TaskModel

@Database(entities = arrayOf(TaskModel::class), version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        private const val TAG = "TaskDatabase"

        private lateinit var INSTANCE: TaskDatabase
        lateinit var dbThread: DbThread

        fun init(context: Context) {
            INSTANCE = getDatabase(context.applicationContext, TaskDatabase::class.java, DATABASE_NAME)
                    .build()
            dbThread = DbThread(threadName = "DbThread")
            dbThread.start()
        }

        fun taskDao() = INSTANCE.taskDao()

        private fun getDatabase(appContext: Context, klass: Class<TaskDatabase>, dbName: String): RoomDatabase.Builder<TaskDatabase> {
            return if (DATABASE_IN_MEMORY_TEST)
                Room.inMemoryDatabaseBuilder(appContext, klass)
            else
                Room.databaseBuilder(appContext, klass, dbName)
        }
    }
}
