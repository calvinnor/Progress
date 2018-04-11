package com.calvinnor.progress.model

data class TaskState(val state: Int) {

    companion object {
        const val INBOX = 101
        const val PENDING = 102
        const val DONE = 103

        fun buildFrom(state: Int) = TaskState(state)
    }
}

fun TaskState.getTasks(allTasks: List<TaskModel>): MutableList<TaskModel> {
    return allTasks.filter { it.state.state.equals(this.state) }.toMutableList()
}

// Convenience methods for equality
fun TaskState.isInbox() = this.state == TaskState.INBOX
fun TaskState.isPending() = this.state == TaskState.PENDING
fun TaskState.isDone() = this.state == TaskState.DONE
