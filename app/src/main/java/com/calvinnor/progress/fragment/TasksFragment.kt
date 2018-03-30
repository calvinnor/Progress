package com.calvinnor.progress.fragment

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.calvinnor.progress.R
import com.calvinnor.progress.adapter.SwipeController
import com.calvinnor.progress.adapter.TaskAdapter
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.event.TaskAddEvent
import com.calvinnor.progress.event.TaskStateChangeEvent
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.util.Events
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.Subscribe

/**
 * A fragment to display Tasks.
 */
class TasksFragment : BaseFragment() {

    override val fragmentTag = this.javaClass.simpleName
    override val layout = R.layout.fragment_main
    override val menu = R.menu.menu_main

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var showTasks: TaskState

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Events.subscribe(this)
        initialiseTaskAdapter()
    }

    fun setShowTasks(showTasks: TaskState) {
        this.showTasks = showTasks
        taskAdapter.updateItems(showTasks.getTasks(TaskRepo.getTasks()))

        val itemTouchHelper = ItemTouchHelper(SwipeController.buildFor(showTasks))
        itemTouchHelper.attachToRecyclerView(main_task_list)
    }

    @Subscribe
    fun onTaskStateChanged(taskStateChangeEvent: TaskStateChangeEvent) {
        taskAdapter.updateItems(showTasks.getTasks(TaskRepo.getTasks()))
    }

    @Subscribe
    fun onTaskAdded(taskAddEvent: TaskAddEvent) {
        if (showTasks == TaskState.COMPLETED) return // NO-OP
        taskAdapter.updateItems(showTasks.getTasks(TaskRepo.getTasks()))
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
