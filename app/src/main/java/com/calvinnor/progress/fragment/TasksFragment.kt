package com.calvinnor.progress.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.calvinnor.progress.R
import com.calvinnor.progress.adapter.TaskAdapter
import com.calvinnor.progress.adapter.TaskSwipeHandler
import com.calvinnor.progress.contract.TasksListener
import com.calvinnor.progress.event.TaskStateChangeEvent
import com.calvinnor.progress.event.TaskUpdateEvent
import com.calvinnor.progress.event.UserEvents
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.util.Events
import com.calvinnor.progress.util.swap
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.Subscribe

/**
 * A fragment to display Tasks.
 */
class TasksFragment : BaseFragment(), TaskSwipeHandler.TaskSwipeListener {

    override val fragmentTag = this.javaClass.simpleName
    override val layout = R.layout.fragment_main
    override val menu = R.menu.menu_main

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var showTasks: TaskState
    private lateinit var taskListener: TasksListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TasksListener) taskListener = context
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Events.subscribe(this)
        initialiseTaskAdapter()
    }

    fun setShowTasks(showTasks: TaskState) {
        this.showTasks = showTasks
        taskAdapter.updateItems(showTasks.getTasks(dataProxy.getTasks()))

        val itemTouchHelper = ItemTouchHelper(TaskSwipeHandler.buildFor(showTasks, this))
        itemTouchHelper.attachToRecyclerView(main_task_list)
    }

    override fun onTaskSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        val taskModel = (viewHolder as TaskAdapter.TaskViewHolder).taskModel
        dataProxy.setComplete(taskModel, when (direction) {
            ItemTouchHelper.LEFT -> false
            ItemTouchHelper.RIGHT -> true
            else -> false
        })
    }

    override fun onTaskMoved(source: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        taskAdapter.apply {
            taskList.swap(source!!.adapterPosition, target!!.adapterPosition)
            notifyItemMoved(source.adapterPosition, target.adapterPosition)
        }
        return true
    }

    @Subscribe
    fun onTaskStateChanged(taskStateChangeEvent: TaskStateChangeEvent) {
        taskAdapter.updateItems(showTasks.getTasks(dataProxy.getTasks()))
    }

    @Subscribe
    fun onTaskAdded(taskAddEvent: UserEvents.TaskAdd) {
        if (showTasks == TaskState.COMPLETED) return // NO-OP
        taskAdapter.updateItems(showTasks.getTasks(dataProxy.getTasks()))
    }

    @Subscribe
    fun onTaskEdit(taskEditEvent: UserEvents.TaskEdit) {
        taskListener.onTaskSelected(taskEditEvent.task)
    }

    @Subscribe
    fun onTaskUpdate(taskUpdateEvent: TaskUpdateEvent) {
        taskAdapter.updateItem(taskUpdateEvent.task)
    }

    private fun initialiseTaskAdapter() {
        taskAdapter = TaskAdapter()
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.task_item_divider, null)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        main_task_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }
}
