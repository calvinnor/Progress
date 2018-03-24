package com.calvinnor.accomplish.activity

import android.view.MenuItem
import com.calvinnor.accomplish.R
import com.calvinnor.accomplish.fragment.MainActivityFragment

class MainActivity : BaseActivity() {

    override val contentLayout = R.layout.activity_main
    override val menuLayout = R.menu.menu_main
    override val fragment = MainActivityFragment()
    override val fragmentContainer = R.id.main_fragment_container

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
