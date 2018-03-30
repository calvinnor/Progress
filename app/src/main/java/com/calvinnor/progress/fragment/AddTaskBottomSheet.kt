package com.calvinnor.progress.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import com.calvinnor.progress.R
import com.calvinnor.progress.data_layer.TaskRepo
import com.calvinnor.progress.model.TaskModel
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*

/**
 * Show the Add Task as a Bottom Sheet.
 */
class AddTaskBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddTaskBottomSheet"
    }

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

            val taskModel = TaskModel.buildFrom(dialog.task_add_content.text.toString(), isComplete = false)
            TaskRepo.insertTask(taskModel)
            dismiss()
        }
    }
}
