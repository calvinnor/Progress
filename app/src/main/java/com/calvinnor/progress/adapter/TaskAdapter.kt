package com.calvinnor.progress.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calvinnor.progress.R
import com.calvinnor.progress.model.TaskModel
import kotlinx.android.synthetic.main.layout_task_item.view.*

/**
 * A simple adapter to display Tasks
 */
class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val taskList: MutableList<TaskModel> = mutableListOf()

    override fun getItemCount() = taskList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        return TaskViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_task_item, parent, false))
    }

    fun addItem(taskModel: TaskModel) {
        taskList.add(taskModel)
        notifyItemInserted(taskList.size - 1)
    }

    fun setItems(taskList: List<TaskModel>) {
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.taskList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int) = taskList.get(position)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(taskModel: TaskModel) {
            itemView.task_item_content.text = taskModel.title
        }
    }
}
