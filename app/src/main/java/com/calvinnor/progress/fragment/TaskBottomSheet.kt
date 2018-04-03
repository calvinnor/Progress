package com.calvinnor.progress.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.widget.RadioButton
import com.calvinnor.progress.R
import com.calvinnor.progress.app.ProgressApp
import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskPriority
import com.calvinnor.progress.model.TaskPriority.Companion.P1
import com.calvinnor.progress.model.TaskPriority.Companion.P2
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import com.calvinnor.progress.model.getContentColor
import com.calvinnor.progress.model.getPrimaryColor
import com.calvinnor.progress.util.fadeColors
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*
import javax.inject.Inject


/**
 * Show the Add Task as a Bottom Sheet.
 */
class TaskBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "TaskBottomSheet"

        const val ARGS_TASK_ID = "args_task_id"

        /**
         * Creates a Bottom Sheet to add a task.
         */
        fun newInstance(): TaskBottomSheet {
            return TaskBottomSheet()
        }

        /**
         * Creates a Bottom Sheet to edit a task.
         */
        fun newInstance(task: TaskModel): TaskBottomSheet {
            val bottomSheetFragment = TaskBottomSheet()
            val args = Bundle().apply {
                putString(ARGS_TASK_ID, task.id)
            }
            bottomSheetFragment.arguments = args
            return bottomSheetFragment
        }
    }

    private lateinit var bottomDialog: Dialog
    private var editTask: TaskModel? = null
    private var taskPriority = TaskPriority(P3)

    @Inject
    protected lateinit var dataProxy: DataProxy

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomDialog = super.onCreateDialog(savedInstanceState)
        ProgressApp.tasksComponent.inject(this)

        bottomDialog.setContentView(LayoutInflater.from(context)
                .inflate(R.layout.fragment_add_task_bottom_sheet, null))
        initDialog()
        initEditTask()
        return bottomDialog
    }

    private fun initDialog() {
        bottomDialog.add_task_done.setOnClickListener {
            if (bottomDialog.task_add_content.text.isEmpty()) {
                dismiss()
            }

            if (editTask != null) { // Edit scenario
                val newTask = editTask?.copy(
                        editTask!!.id,
                        bottomDialog.task_add_content.text.toString(),
                        editTask!!.isComplete,
                        taskPriority)

                if (newTask == null) return@setOnClickListener // Shut up Kotlin
                dataProxy.updateTask(newTask)

            } else { // New scenario
                val taskModel = TaskModel.buildFrom(bottomDialog.task_add_content.text.toString(), false, taskPriority)
                dataProxy.insertTask(taskModel)
            }
            dismiss()
        }

        bottomDialog.task_add_priority_group.setOnCheckedChangeListener { group, checkedId ->

            val oldColor = taskPriority.getPrimaryColor(context)
            val oldTextColor = taskPriority.getContentColor(context)

            taskPriority = when (checkedId) {
                R.id.task_add_priority_p1 -> TaskPriority(P1)
                R.id.task_add_priority_p2 -> TaskPriority(P2)
                R.id.task_add_priority_p3 -> TaskPriority(P3)
                else -> TaskPriority(P3)
            }

            fadeColors(oldColor, taskPriority.getPrimaryColor(context)) { color ->
                setPrimaryColor(color)
            }

            fadeColors(oldTextColor, taskPriority.getContentColor(context)) { color ->
                setContentColor(color)
            }
        }
    }

    private fun initEditTask() {
        if (arguments == null) { // Use defaults
            setPrimaryColor(taskPriority.getPrimaryColor(context))
            setContentColor(taskPriority.getContentColor(context))
            return
        }

        val taskModel = dataProxy.getTask(arguments.getString(ARGS_TASK_ID))
        checkNotNull(taskModel, { "Task Model cannot be null: ${arguments.getString(ARGS_TASK_ID)}" })
        if (taskModel == null) return // Shut up Kotlin
        editTask = taskModel

        taskPriority = taskModel.priority
        bottomDialog.task_add_content.setText(taskModel.title)

        setPrimaryColor(taskModel.priority.getPrimaryColor(context))
        setContentColor(taskPriority.getContentColor(context))
    }

    private fun setPrimaryColor(color: Int) {
        bottomDialog.task_add_container.setBackgroundColor(color)
    }

    private fun setContentColor(color: Int) {
        bottomDialog.add_task_done.setColorFilter(color)
        bottomDialog.add_task_title.setTextColor(color)
        bottomDialog.task_add_content.apply {
            setTextColor(color)
            setHintTextColor(color)
        }

        for (position in 0..bottomDialog.task_add_priority_group.childCount - 1) {
            val radioButton = bottomDialog.task_add_priority_group.getChildAt(position)
            if (radioButton is RadioButton) radioButton.setTextColor(color)
        }
    }
}
