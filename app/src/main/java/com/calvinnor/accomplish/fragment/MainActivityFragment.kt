package com.calvinnor.accomplish.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.calvinnor.accomplish.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment() {

    override val fragmentTag = this.javaClass.simpleName;
    override val layout = R.layout.fragment_main

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task_add_action.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {
        Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
    }
}
