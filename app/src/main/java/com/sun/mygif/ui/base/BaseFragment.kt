package com.sun.mygif.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sun.mygif.R
import com.sun.mygif.data.model.ActionBarInfo
import com.sun.mygif.data.model.GifSmall
import com.sun.mygif.service.GifHeadService
import com.sun.mygif.ui.home.ActionBarFragment
import kotlinx.android.synthetic.main.message_toast.view.*

abstract class BaseFragment : Fragment() {

    protected abstract val layoutResource: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initData()
    }

    protected abstract fun initComponents()

    protected abstract fun initData()

    protected fun toastMsg(msg: String){
        context?.run {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun toastMsg(resId: Int){
        context?.run {
            Toast.makeText(context, this.getString(resId), Toast.LENGTH_SHORT).show()
        }
    }

    protected fun toastMsg(iconId: Int, msgId: Int){
        context?.run {
            Toast.makeText(this, this.getString(msgId), Toast.LENGTH_SHORT).apply {
                view = View.inflate(context, R.layout.message_toast, null).apply {
                    text.apply {
                        text = context.getString(msgId)
                        setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0)
                    }
                }
            }.show()
        }
    }

    protected fun replaceFragment(id: Int, fragment: Fragment, addToBackStack: Boolean) =
        activity?.supportFragmentManager?.beginTransaction()?.replace(id, fragment)?.apply {
            if (addToBackStack) addToBackStack(null)
        }?.commit()

    protected fun addFragment(id: Int, fragment: Fragment, addToBackStack: Boolean) =
        activity?.supportFragmentManager?.beginTransaction()?.add(id, fragment)?.apply {
            if (addToBackStack) addToBackStack(null)
        }?.commit()

    protected fun backToPreviousFragment() = activity?.supportFragmentManager?.run {
        if (backStackEntryCount > 0) popBackStack()
    }

    protected open fun initActionBar(title: String, iconId: Int) = replaceFragment(
        id = R.id.frameActionBar,
        fragment = ActionBarFragment.newInstance(ActionBarInfo(title, iconId)),
        addToBackStack = false
    )

    protected open fun openGifHead(gifSmall: GifSmall?) {
        activity?.run {
            startService(GifHeadService.getIntent(this, gifSmall))
            finish()
        }
    }
}
