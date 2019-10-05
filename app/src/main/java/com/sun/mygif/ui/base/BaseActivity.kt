package com.sun.mygif.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        initComponent()
        initData()
    }

    protected abstract fun initComponent()

    protected abstract fun initData()

    protected fun openFragment(id: Int, fragment: Fragment, addToBackStack: Boolean) =
        supportFragmentManager.beginTransaction().replace(id, fragment).apply {
            if (addToBackStack) addToBackStack(null)
        }.commit()
}
