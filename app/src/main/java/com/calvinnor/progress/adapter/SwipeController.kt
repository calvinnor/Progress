package com.calvinnor.progress.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.util.swap

class SwipeController(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    companion object {
        private const val NO_SWIPE = 0
        private const val MOVE_VERTICAL = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        private const val LEFT_SWIPE = ItemTouchHelper.LEFT
        private const val RIGHT_SWIPE = ItemTouchHelper.RIGHT

        fun buildFor(taskState: TaskState): SwipeController? {
            return when (taskState) {
                TaskState.ALL -> SwipeController(MOVE_VERTICAL, NO_SWIPE)
                TaskState.PENDING -> SwipeController(MOVE_VERTICAL, RIGHT_SWIPE)
                TaskState.COMPLETED -> SwipeController(MOVE_VERTICAL, LEFT_SWIPE)
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        val taskAdapter = recyclerView?.adapter as TaskAdapter
        taskAdapter.taskList.swap(viewHolder!!.adapterPosition, target!!.adapterPosition)
        taskAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        val taskModel = (viewHolder as TaskAdapter.TaskViewHolder).taskModel
        TaskRepo.setComplete(taskModel, when (direction) {
            ItemTouchHelper.LEFT -> false
            ItemTouchHelper.RIGHT -> true
            else -> false
        })
    }
}

