package com.calvinnor.progress.activity

import android.os.Bundle
import com.calvinnor.progress.R
import com.calvinnor.progress.fragment.AddTaskBottomSheet
import com.calvinnor.progress.fragment.MainActivityFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val contentLayout = R.layout.activity_main
    override val fragment = MainActivityFragment()
    override val fragmentContainer = R.id.main_fragment_container

    private var addTaskSheet: AddTaskBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_add_task_fab.setOnClickListener {
            addTaskSheet = AddTaskBottomSheet()
            addTaskSheet!!.show(supportFragmentManager, AddTaskBottomSheet.TAG)
        }
    }
}
