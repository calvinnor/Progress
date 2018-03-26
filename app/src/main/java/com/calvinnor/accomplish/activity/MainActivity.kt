package com.calvinnor.accomplish.activity

import com.calvinnor.accomplish.R
import com.calvinnor.accomplish.fragment.MainActivityFragment

class MainActivity : BaseActivity() {

    override val contentLayout = R.layout.activity_main
    override val fragment = MainActivityFragment()
    override val fragmentContainer = R.id.main_fragment_container
}
