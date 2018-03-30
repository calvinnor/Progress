package com.calvinnor.progress.model

enum class TaskState {

    ALL {
        override fun getTasks(allTasks: List<TaskModel>) = allTasks.toMutableList()
    },

    PENDING {
        override fun getTasks(allTasks: List<TaskModel>) = allTasks.filter { it.isComplete == false }.toMutableList()
    },

    COMPLETED {
        override fun getTasks(allTasks: List<TaskModel>) = allTasks.filter { it.isComplete == true }.toMutableList()
    };

    abstract fun getTasks(allTasks: List<TaskModel>): MutableList<TaskModel>
}
