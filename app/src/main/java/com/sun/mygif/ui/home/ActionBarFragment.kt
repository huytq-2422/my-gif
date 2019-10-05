package com.sun.mygif.ui.home

import android.os.Bundle
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.ActionBarInfo
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.search.SearchFragment
import com.sun.mygif.utils.EXTRA_ACTION_BAR_INFO
import kotlinx.android.synthetic.main.fragment_action_bar.*

class ActionBarFragment : BaseFragment(), View.OnClickListener {
    override val layoutResource: Int = R.layout.fragment_action_bar

    override fun initComponents() {
        searchActionBar?.setOnClickListener(this)
        iconActionBar?.setOnClickListener(this)
    }

    override fun initData() {
        val actionBarInfo = arguments?.getParcelable(EXTRA_ACTION_BAR_INFO) as ActionBarInfo
        titleActionBar.text = actionBarInfo.title
        iconActionBar.setImageResource(actionBarInfo.iconId)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.searchActionBar) {
            replaceFragment(R.id.constraintMain, SearchFragment.newInstance(null), true)
        } else if (v?.id == R.id.iconActionBar) {
            openGifHead(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(message: ActionBarInfo): ActionBarFragment = ActionBarFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ACTION_BAR_INFO, message)
            }
        }
    }
}
