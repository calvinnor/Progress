package com.calvinnor.progress.adapter

import android.support.v7.util.DiffUtil
import com.calvinnor.progress.model.TaskModel

class TaskDiffUtil(val oldList: List<TaskModel>, val newList: List<TaskModel>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList.get(oldItemPosition).id == newList.get(newItemPosition).id

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
}
