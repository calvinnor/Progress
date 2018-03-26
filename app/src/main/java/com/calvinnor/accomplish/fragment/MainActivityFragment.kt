package com.calvinnor.accomplish.fragment

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.calvinnor.accomplish.R
import com.calvinnor.accomplish.adapter.TaskAdapter
import com.calvinnor.accomplish.data_layer.TaskRepo
import com.calvinnor.accomplish.model.TaskModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_task_add.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment(),
        TaskRepo.TasksListener {

    override val fragmentTag = this.javaClass.simpleName;
    override val layout = R.layout.fragment_main
    override val menu = R.menu.menu_main

    private lateinit var taskAdapter: TaskAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TaskRepo.registerListener(this)

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

        task_add_action.setOnClickListener {
            onClick()
        }

        // Fetch all Tasks
        TaskRepo.getTasks()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> {
                TaskRepo.deleteAllTasks(Runnable { taskAdapter.clearItems() })
                return true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTasksFetched(taskList: List<TaskModel>) {
        taskAdapter.setItems(taskList)
    }

    private fun onClick() {
        val taskModel = TaskModel(task_add_content.text.toString(), isComplete = false);
        taskAdapter.addItem(taskModel)
        TaskRepo.insertTask(taskModel, Runnable { })
        task_add_content.text.clear()
    }
}
