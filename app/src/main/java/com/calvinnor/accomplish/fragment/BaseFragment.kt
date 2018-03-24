package com.calvinnor.accomplish.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Base Fragment to inherit from.
 * All common code and abstraction goes here.
 */
abstract class BaseFragment : Fragment() {

    companion object {
        private val NO_LAYOUT = -1
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return getInflatedView(inflater)
    }

    /**
     * Override this method to provide a fragment layout.
     *
     * @return The layout resource ID.
     */
    @LayoutRes
    open protected val layout = NO_LAYOUT

    /**
     * Override this value to provide a fragment tag.
     * This will be used in Fragment Transactions.
     *
     * @return A string representing the fragment tag.
     */
    abstract val fragmentTag: String

    private fun getInflatedView(inflater: LayoutInflater): View? {
        val activityLayout = layout
        return if (activityLayout == NO_LAYOUT) null
        else inflater.inflate(layout, null, false)
    }
}
