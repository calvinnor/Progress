package com.calvinnor.progress.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.calvinnor.progress.R
import com.calvinnor.progress.app.ProgressApp
import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.model.*
import com.calvinnor.progress.model.TaskPriority.Companion.P1
import com.calvinnor.progress.model.TaskPriority.Companion.P2
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import com.calvinnor.progress.model.TaskState.Companion.INBOX
import com.calvinnor.progress.util.fadeColors
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*
import java.util.*
import java.util.Calendar.*
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
        bottomDialog.add_task_date.setOnClickListener { showDatePicker() }
        bottomDialog.add_task_time.setOnClickListener { showTimePicker() }
        bottomDialog.add_task_priority.setOnClickListener { showPopup(it) }

        bottomDialog.add_task_done.setOnClickListener {
            if (bottomDialog.task_add_title.text.isEmpty()) {
                dismiss()
            }

            if (editTask != null) { // Edit scenario
                val newTask = editTask?.copy(
                        editTask!!.id,
                        bottomDialog.task_add_title.text.toString(),
                        bottomDialog.task_add_description.text.toString(),
                        editTask!!.state,
                        taskPriority)

                if (newTask == null) return@setOnClickListener // Shut up Kotlin
                dataProxy.updateTask(newTask)

            } else { // New scenario
                val taskModel = TaskModel.buildFrom(bottomDialog.task_add_title.text.toString(), bottomDialog.task_add_description.text.toString(), TaskState(INBOX), taskPriority)
                dataProxy.insertTask(taskModel)
            }
            dismiss()
        }
    }

    private fun setPriority(checkedId: Int) {
        val oldColor = taskPriority.getPrimaryColor(context)
        val oldTextColor = taskPriority.getContentColor(context)

        taskPriority = when (checkedId) {
            R.id.priority_p1 -> TaskPriority(P1)
            R.id.priority_p2 -> TaskPriority(P2)
            R.id.priority_p3 -> TaskPriority(P3)
            else -> TaskPriority(P3)
        }

        fadeColors(oldColor, taskPriority.getPrimaryColor(context)) { color ->
            setPrimaryColor(color)
        }

        fadeColors(oldTextColor, taskPriority.getContentColor(context)) { color ->
            setContentColor(color)
        }
    }

    private fun showTimePicker() {
        val currentTime = GregorianCalendar()
        val timePickerDialog = TimePickerDialog(context, null,
                currentTime.get(HOUR_OF_DAY),
                currentTime.get(MINUTE),
                false)
        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val currentTime = GregorianCalendar()
        val datePicker = DatePickerDialog(context, null,
                currentTime.get(YEAR),
                currentTime.get(MONTH),
                currentTime.get(DAY_OF_MONTH))
        datePicker.datePicker.minDate = currentTime.timeInMillis
        datePicker.show()
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(R.menu.menu_priority, popup.getMenu())

        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

            override fun onMenuItemClick(item: MenuItem): Boolean {
                setPriority(item.itemId)
                return true
            }
        })
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show()
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
        bottomDialog.task_add_title.setText(taskModel.title)
        bottomDialog.task_add_description.setText(taskModel.description)

        setPrimaryColor(taskModel.priority.getPrimaryColor(context))
        setContentColor(taskPriority.getContentColor(context))
    }

    private fun setPrimaryColor(color: Int) {
        bottomDialog.task_add_container.setBackgroundColor(color)
    }

    private fun setContentColor(color: Int) {
        bottomDialog.add_task_date.setColorFilter(color)
        bottomDialog.add_task_time.setColorFilter(color)
        bottomDialog.add_task_priority.setColorFilter(color)
        bottomDialog.add_task_done.setColorFilter(color)
        bottomDialog.add_task_title.setTextColor(color)
        bottomDialog.task_add_title.apply {
            setTextColor(color)
            setHintTextColor(color)
        }
        bottomDialog.task_add_description.apply {
            setTextColor(color)
            setHintTextColor(color)
        }
    }
}
