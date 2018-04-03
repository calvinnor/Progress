package com.calvinnor.progress.activity

import android.os.Bundle
import com.calvinnor.progress.R
import com.calvinnor.progress.contract.TasksListener
import com.calvinnor.progress.event.TasksLoadedEvent
import com.calvinnor.progress.fragment.TaskBottomSheet
import com.calvinnor.progress.fragment.TasksFragment
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskState
import com.calvinnor.progress.util.Events
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe

class TasksActivity : BaseActivity(), TasksListener {

    override val contentLayout = R.layout.activity_main
    override val fragment = TasksFragment()
    override val fragmentContainer = R.id.main_fragment_container

    private var taskBottomSheet: TaskBottomSheet? = null
    private var showTasks = TaskState.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_add_task_fab.setOnClickListener {
            taskBottomSheet = TaskBottomSheet.newInstance()
            taskBottomSheet!!.show(supportFragmentManager, TaskBottomSheet.TAG)
        }

        main_bottom_navigation_view.setOnNavigationItemSelectedListener {
            showTasks = when (it.itemId) {
                R.id.navigation_inbox -> TaskState.ALL
                R.id.navigation_in_progress -> TaskState.PENDING
                R.id.navigation_done -> TaskState.COMPLETED
                else -> TaskState.ALL
            }
            fragment.setShowTasks(showTasks)
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onStart() {
        super.onStart()
        Events.subscribe(this)
    }

    override fun onStop() {
        Events.unsubscribe(this)
        super.onStop()
    }

    override fun onTaskSelected(task: TaskModel) {
        taskBottomSheet = TaskBottomSheet.newInstance(task)
        taskBottomSheet!!.show(supportFragmentManager, TaskBottomSheet.TAG)
    }

    @Subscribe(sticky = true)
    fun onTasksLoaded(tasksLoadedEvent: TasksLoadedEvent) {
        fragment.setShowTasks(showTasks)
    }
}
