package com.sun.mygif.recyclerview

import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.DisplayMetrics

private const val SLOW_SPEED = 255f
private const val FAST_SPEED = 2f

class SpeedyStaggeredGridLayoutManager(
    spanCount: Int,
    orientation: Int
) : StaggeredGridLayoutManager(spanCount, orientation) {

    private var speed = SLOW_SPEED

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) =
        startSmoothScroll(object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float =
                speed / displayMetrics.densityDpi
        }.apply { targetPosition = position })

    fun scrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int, speedMode: Int) {
        speed = if (speedMode == SPEEDY) FAST_SPEED else SLOW_SPEED
        smoothScrollToPosition(recyclerView, state, position)
    }

    fun scrollToBottom(recyclerView: RecyclerView){
        scrollToPosition(recyclerView, null, itemCount, SLOWLY)
    }

    fun scrollToTop(recyclerView: RecyclerView){
        scrollToPosition(recyclerView, null, 0, SPEEDY)
    }

    companion object {
        const val SPEEDY = 1
        const val SLOWLY = 2
    }
}
