package com.calvinnor.progress.navigation

import android.content.Context
import android.support.design.widget.NavigationView
import android.util.AttributeSet
import android.view.LayoutInflater
import com.calvinnor.progress.BuildConfig
import com.calvinnor.progress.R
import kotlinx.android.synthetic.main.layout_navigation_header.view.*

/**
 * Wrapper for showing the Navigation Drawer.
 *
 * Internally handles item selection and propagates them to listeners.
 */
class NavigationDrawerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : NavigationView(context, attrs, defStyleAttr) {

    init {
        inflateNavigationLayout()
        populateView()
    }

    private fun inflateNavigationLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_navigation_drawer, this, true)
    }

    private fun populateView() {
        nav_header_app_version.text = BuildConfig.VERSION_NAME
    }
}
