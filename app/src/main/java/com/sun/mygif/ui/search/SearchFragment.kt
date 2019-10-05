package com.sun.mygif.ui.search

import android.os.Bundle
import com.sun.mygif.R
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.utils.EXTRA_SEARCH_KEY

class SearchFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_search

    private var searchKey: String? = null

    override fun initComponents() {
    }

    override fun initData() {
        searchKey = arguments?.getString(EXTRA_SEARCH_KEY)
        replaceFragment(R.id.frameSearchBox, SearchHeaderFragment.newInstance(searchKey), false)
    }

    companion object {
        @JvmStatic
        fun newInstance(message: String?) = SearchFragment().apply {
            message?.let { arguments = Bundle().apply { putString(EXTRA_SEARCH_KEY, it) } }
        }
    }
}
