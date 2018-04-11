package com.calvinnor.progress.adapter

import android.graphics.drawable.GradientDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calvinnor.progress.R
import com.calvinnor.progress.event.UserEvents
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.getPrimaryColor
import com.calvinnor.progress.util.Events
import com.calvinnor.progress.util.positionOf
import kotlinx.android.synthetic.main.layout_task_item.view.*

/**
 * A simple adapter to display Tasks
 */
class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var taskList: MutableList<TaskModel> = mutableListOf()

    override fun getItemCount() = taskList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        return TaskViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_task_item, parent, false))
    }

    fun setItems(taskList: MutableList<TaskModel>) {
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    fun removeItem(removedTask: TaskModel) {
        val taskPosition = taskList.positionOf(removedTask)
        if (taskPosition == -1) return // We do not have this task
        taskList.removeAt(taskPosition)
        notifyItemRemoved(taskPosition)
    }

    fun updateItems(newList: MutableList<TaskModel>) {
        if (taskList.isEmpty()) {
            setItems(newList)
            return
        }
        val diffResult = DiffUtil.calculateDiff(TaskDiffUtil(taskList, newList))
        taskList.clear()
        taskList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int) = taskList.get(position)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var taskModel: TaskModel

        fun bind(taskModel: TaskModel) {
            this.taskModel = taskModel
            itemView.task_item_content.text = taskModel.title
            val shapeDrawable = itemView.task_item_priority_indicator.background as GradientDrawable
            shapeDrawable.setColor(taskModel.priority.getPrimaryColor(itemView.context))
            itemView.setOnClickListener { view ->
                Events.post(UserEvents.TaskEdit(taskModel))
            }
        }
    }
}
