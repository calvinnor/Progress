package com.calvinnor.progress.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import com.calvinnor.progress.R
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.model.TaskModel
import com.calvinnor.progress.model.TaskPriority
import com.calvinnor.progress.model.TaskPriority.Companion.P1
import com.calvinnor.progress.model.TaskPriority.Companion.P2
import com.calvinnor.progress.model.TaskPriority.Companion.P3
import com.calvinnor.progress.model.getColor
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*


/**
 * Show the Add Task as a Bottom Sheet.
 */
class AddTaskBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddTaskBottomSheet"
    }

    private var taskPriority = TaskPriority(P3)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(LayoutInflater.from(context)
                .inflate(R.layout.fragment_add_task_bottom_sheet, null))
        initDialog(dialog)
        return dialog
    }

    private fun initDialog(dialog: Dialog) {
        dialog.add_task_done.setOnClickListener {
            if (dialog.task_add_content.text.isEmpty()) {
                dismiss()
            }

            val taskModel = TaskModel.buildFrom(dialog.task_add_content.text.toString(), false, taskPriority)
            TaskRepo.insertTask(taskModel)
            dismiss()
        }

        dialog.task_add_priority_group.setOnCheckedChangeListener { group, checkedId ->

            val oldColor = taskPriority.getColor(context)

            taskPriority = when (checkedId) {
                R.id.task_add_priority_p1 -> TaskPriority(P1)
                R.id.task_add_priority_p2 -> TaskPriority(P2)
                R.id.task_add_priority_p3 -> TaskPriority(P3)
                else -> TaskPriority(P3)
            }

            val anim = ValueAnimator.ofInt(oldColor, taskPriority.getColor(context))
            anim.setEvaluator(ArgbEvaluator())
            anim.addUpdateListener { animation ->
                dialog.task_add_container.setBackgroundColor(animation.animatedValue as Int)
            }
            anim.start()
        }
    }
}
