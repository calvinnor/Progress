package com.calvinnor.progress.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import com.calvinnor.progress.R
import com.calvinnor.progress.fragment.TaskBottomSheet

/**
 * Semi-transparent activity for adding tasks via "Share" button.
 */
class AddTaskActivity : BaseActivity() {

    companion object {
        private const val TEXT_MIME_TYPE = "text/plain"
    }

    override val hasToolbar = false
    override val contentLayout = R.layout.activity_add_task
    override val fragmentContainer = R.id.main_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null) return

        val action = intent.action
        val mimeType = intent.type

        if (Intent.ACTION_SEND != action || TEXT_MIME_TYPE != mimeType) return

        val taskTitle = intent.getStringExtra(Intent.EXTRA_TEXT)
        val taskBottomSheet = TaskBottomSheet.newInstance(taskTitle)
        taskBottomSheet.show(supportFragmentManager, TaskBottomSheet.TAG)
    }
}
