package com.calvinnor.progress.activity

import com.calvinnor.progress.R
import com.calvinnor.progress.fragment.MainActivityFragment

class MainActivity : BaseActivity() {

    override val contentLayout = R.layout.activity_main
    override val fragment = MainActivityFragment()
    override val fragmentContainer = R.id.main_fragment_container
}
