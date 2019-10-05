package com.sun.mygif.utils

import android.support.v4.app.FragmentActivity

class ScreenHelper {
    companion object {
        @JvmStatic
        fun getCurrentFragment(fragmentActivity: FragmentActivity, resId: Int) =
            fragmentActivity.supportFragmentManager.findFragmentById(resId)

        @JvmStatic
        fun isShowingFirstScreen(fragmentActivity: FragmentActivity, resId: Int): Boolean =
            getCurrentFragment(fragmentActivity, resId)?.isFirstScreen() ?: false

        @JvmStatic
        fun isShowingSearchScreen(fragmentActivity: FragmentActivity, resId: Int): Boolean =
            getCurrentFragment(fragmentActivity, resId)?.isSearchScreen() ?: false

        @JvmStatic
        fun isShowingDetailScreen(fragmentActivity: FragmentActivity, resId: Int): Boolean =
            getCurrentFragment(fragmentActivity, resId)?.isShowingDetailScreen() ?: false

        @JvmStatic
        fun backOutDetailScreen(fragmentActivity: FragmentActivity, resId: Int) =
            with(fragmentActivity.supportFragmentManager) {
                while (isShowingDetailScreen(fragmentActivity, resId)) popBackStackImmediate()
            }

        @JvmStatic
        fun backToFirstScreen(fragmentActivity: FragmentActivity) = with(fragmentActivity.supportFragmentManager) {
            for (counter in 0 until backStackEntryCount) popBackStack()
        }
    }
}
