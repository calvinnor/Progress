package com.calvinnor.progress.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.ListPopupWindow
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.calvinnor.progress.R
import com.calvinnor.progress.contract.DataProxy
import com.calvinnor.progress.injection.dependencyComponent
import com.calvinnor.progress.model.*
import com.calvinnor.progress.model.TaskPriority.Companion.P1
import com.calvinnor.progress.model.TaskPriority.Companion.P2
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import com.calvinnor.progress.model.TaskState.Companion.INBOX
import com.calvinnor.progress.service.SystemServices
import com.calvinnor.progress.util.fadeColors
import com.calvinnor.progress.util.getFormattedDate
import com.calvinnor.progress.util.getFormattedTime
import com.calvinnor.progress.util.getString
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
        const val ARGS_TASK_TITLE = "args_task_title"

        /**
         * Creates a Bottom Sheet to add a task.
         */
        fun newInstance(): TaskBottomSheet {
            return TaskBottomSheet()
        }

        /**
         * Creates a Bottom Sheet to add a task with provided title.
         */
        fun newInstance(taskTitle: String): TaskBottomSheet {
            val bottomSheetFragment = TaskBottomSheet()
            bottomSheetFragment.arguments = Bundle().apply {
                putString(ARGS_TASK_TITLE, taskTitle)
            }
            return bottomSheetFragment
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

    private lateinit var rootView: Dialog
    private var editTask: TaskModel? = null
    private var taskPriority = TaskPriority(P3)
    private var taskTime = Calendar.getInstance()

    private lateinit var priorityPopupWindow: ListPopupWindow

    @Inject
    protected lateinit var dataProxy: DataProxy

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        rootView.setOnDismissListener {
            activity.finish()
        }

        dependencyComponent.inject(this)

        rootView.setContentView(LayoutInflater.from(context)
                .inflate(R.layout.fragment_add_task_bottom_sheet, null))
        initDialog()
        initEditTask()
        initMembers()
        return rootView
    }

    private fun initDialog() {
        rootView.add_task_date.setOnClickListener { showDatePicker() }
        rootView.add_task_time.setOnClickListener { showTimePicker() }
        rootView.add_task_priority.setOnClickListener { showPopup() }

        rootView.add_task_done.setOnClickListener {
            if (rootView.task_add_title.text.isEmpty()) {
                dismiss()
            }

            if (editTask != null) { // Edit scenario
                val newTask = editTask?.copy(
                        id = editTask!!.id,
                        title = rootView.task_add_title.getString(),
                        description = rootView.task_add_description.getString(),
                        state = editTask!!.state,
                        priority = taskPriority) ?: return@setOnClickListener

                dataProxy.updateTask(newTask)

            } else { // New scenario
                val taskModel = TaskModel.buildFrom(rootView.task_add_title.text.toString(), rootView.task_add_description.text.toString(), TaskState(INBOX), taskPriority, dateTime = taskTime)
                dataProxy.insertTask(taskModel)
                SystemServices.alarmScheduler.scheduleAlarm(taskModel)
            }
            dismiss()
        }
    }

    private fun setPriority(newPriority: TaskPriority) {
        val oldColor = taskPriority.getPrimaryColor(context)
        val oldTextColor = taskPriority.getContentColor(context)

        rootView.add_task_priority_text.text = newPriority.getText(context)

        fadeColors(oldColor, newPriority.getPrimaryColor(context)) { color ->
            setPrimaryColor(color)
        }

        fadeColors(oldTextColor, newPriority.getContentColor(context)) { color ->
            setContentColor(color)
        }

        taskPriority = newPriority
    }

    private fun showTimePicker() {
        val currentTime = GregorianCalendar()
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            rootView.add_task_time_text.text = getFormattedTime(hourOfDay, minute)
            taskTime.apply {
                set(HOUR_OF_DAY, hourOfDay)
                set(MINUTE, minute)
            }
        }

        val timePickerDialog = TimePickerDialog(
                context,
                timePickerListener,
                currentTime.get(HOUR_OF_DAY),
                currentTime.get(MINUTE),
                false)
        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val currentTime = GregorianCalendar()
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            rootView.add_task_date_text.text = getFormattedDate(year, month, dayOfMonth)
            taskTime.apply {
                set(YEAR, year)
                set(MONTH, month)
                set(DAY_OF_MONTH, dayOfMonth)
            }
        }
        val datePicker = DatePickerDialog(
                context,
                datePickerListener,
                currentTime.get(YEAR),
                currentTime.get(MONTH),
                currentTime.get(DAY_OF_MONTH))
        datePicker.datePicker.minDate = currentTime.timeInMillis
        datePicker.show()
    }

    private fun showPopup() = priorityPopupWindow.show()

    private fun initEditTask() {
        if (arguments == null) { // Use defaults
            setDefaultColors()
            return
        }

        val taskTitle = arguments.getString(ARGS_TASK_TITLE)
        if (taskTitle != null) { // Share action
            setDefaultColors()
            rootView.task_add_title.setText(taskTitle)
            return
        }

        val taskModel = dataProxy.getTask(arguments.getString(ARGS_TASK_ID))
        checkNotNull(taskModel, { "Task Model cannot be null: ${arguments.getString(ARGS_TASK_ID)}" })
        if (taskModel == null) return // Shut up Kotlin
        editTask = taskModel

        taskPriority = taskModel.priority
        taskTime = taskModel.dateTime

        rootView.apply {
            task_add_title.setText(taskModel.title)
            task_add_description.setText(taskModel.description)
            add_task_date_text.text = getFormattedDate(taskTime)
            add_task_time_text.text = getFormattedTime(taskTime)
            add_task_priority_text.text = taskPriority.getText(context)
        }

        setPrimaryColor(taskModel.priority.getPrimaryColor(context))
        setContentColor(taskPriority.getContentColor(context))
    }

    private fun setDefaultColors() {
        setPrimaryColor(taskPriority.getPrimaryColor(context))
        setContentColor(taskPriority.getContentColor(context))
    }

    private fun initMembers() {
        val priorityList = arrayOf(TaskPriority(P1), TaskPriority(P2), TaskPriority(P3))
        val priorityAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, priorityList)
        priorityPopupWindow = ListPopupWindow(context).apply {
            isModal = true
            anchorView = rootView.add_task_priority
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position, _ ->
                dismiss()
                setPriority(priorityList[position])
            }
        }
    }

    private fun setPrimaryColor(color: Int) {
        rootView.task_add_container.setBackgroundColor(color)
    }

    private fun setContentColor(color: Int) {
        rootView.apply {
            add_task_date_text.setTextColor(color)
            add_task_date_image.setColorFilter(color)
            add_task_time_text.setTextColor(color)
            add_task_time_image.setColorFilter(color)
            add_task_priority_text.setTextColor(color)
            add_task_priority_image.setColorFilter(color)
            add_task_done.setColorFilter(color)
            add_task_title.setTextColor(color)

            task_add_title.apply {
                setTextColor(color)
                setHintTextColor(color)
            }

            task_add_description.apply {
                setTextColor(color)
                setHintTextColor(color)
            }
        }
    }
}
