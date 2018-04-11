package com.calvinnor.progress.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.model.TaskState.Companion.DONE
import com.calvinnor.progress.model.TaskState.Companion.INBOX
import com.calvinnor.progress.model.TaskState.Companion.PENDING

class TaskSwipeHandler(dragDirs: Int, swipeDirs: Int, private val taskSwipeListener: TaskSwipeListener)
    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    companion object {
        private const val NO_SWIPE = 0
        private const val LEFT_SWIPE = ItemTouchHelper.LEFT
        private const val RIGHT_SWIPE = ItemTouchHelper.RIGHT
        private const val MOVE_HORIZONTAL = LEFT_SWIPE or RIGHT_SWIPE
        private const val MOVE_VERTICAL = ItemTouchHelper.UP or ItemTouchHelper.DOWN

        fun buildFor(taskState: TaskState, taskSwipeListener: TaskSwipeListener): TaskSwipeHandler? {
            return when (taskState.state) {
                INBOX -> TaskSwipeHandler(MOVE_VERTICAL, RIGHT_SWIPE, taskSwipeListener)
                PENDING -> TaskSwipeHandler(MOVE_VERTICAL, MOVE_HORIZONTAL, taskSwipeListener)
                DONE -> TaskSwipeHandler(MOVE_VERTICAL, LEFT_SWIPE, taskSwipeListener)
                else -> TaskSwipeHandler(MOVE_VERTICAL, NO_SWIPE, taskSwipeListener)
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?) = taskSwipeListener.onTaskMoved(viewHolder, target)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) = taskSwipeListener.onTaskSwiped(viewHolder, direction)

    interface TaskSwipeListener {

        fun onTaskSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int)

        fun onTaskMoved(source: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
    }
}

