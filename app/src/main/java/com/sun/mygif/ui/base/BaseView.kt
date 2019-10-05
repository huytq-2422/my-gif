package com.sun.mygif.ui.base

interface BaseView<T> {
    fun setPresenter(presenter: T)
    fun toast(message: String)
    fun showLoading()
    fun hideLoading()
}
