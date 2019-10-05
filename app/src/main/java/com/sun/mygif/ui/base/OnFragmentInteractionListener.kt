package com.sun.mygif.ui.base

import android.support.v4.app.Fragment

interface OnFragmentInteractionListener {
    fun onTabChange(itemId: Int, fragment: Fragment)
    fun onSaveState(gifId: String)
}
