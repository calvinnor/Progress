package com.calvinnor.progress.fragment

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.calvinnor.progress.R
import com.calvinnor.progress.adapter.TaskAdapter
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.event.AddTaskEvent
import com.calvinnor.progress.event.TasksEvent
import com.calvinnor.progress.util.EventBus
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment() {

    override val fragmentTag = this.javaClass.simpleName
    override val layout = R.layout.fragment_main
    override val menu = R.menu.menu_main

    private lateinit var taskAdapter: TaskAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.subscribe(this)
        initialiseTaskAdapter()

        // Fetch all Tasks
        TaskRepo.getTasks()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> {
                TaskRepo.deleteAllTasks(Runnable { taskAdapter.clearItems() })
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTasks(tasksEvent: TasksEvent) = taskAdapter.setItems(tasksEvent.tasksList)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddTask(taskEvent: AddTaskEvent) = taskAdapter.addItem(taskEvent.task)
}
