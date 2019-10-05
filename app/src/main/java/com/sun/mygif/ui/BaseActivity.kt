package com.sun.mygif.ui

import android.os.Bundle
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
}
