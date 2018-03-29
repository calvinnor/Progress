package com.calvinnor.progress.event

import com.calvinnor.progress.model.TaskModel

data class TasksEvent(val tasksList: List<TaskModel>)
