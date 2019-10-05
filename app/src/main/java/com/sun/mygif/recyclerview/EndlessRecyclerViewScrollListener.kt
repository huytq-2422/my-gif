package com.sun.mygif.recyclerview

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {

    private val expandableThreshold = 4
    private val startingPageIndex = 0
    private val visibleThreshold = layoutManager.spanCount
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private var isAtTop = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) = with(layoutManager){
        val firstVisibleItemPosition: Int? = findFirstCompletelyVisibleItemPositions(null).min()
        val lastVisibleItemPosition: Int? = findLastVisibleItemPositions(null).max()

        if (itemCount < previousTotalItemCount) initItemLoading()

        if (loading && itemCount > previousTotalItemCount) finishItemLoading()

        if (!loading && lastVisibleItemPosition!! + visibleThreshold >= itemCount) loadMoreItem()

        if (!isAtTop && firstVisibleItemPosition!! < expandableThreshold) collapseView()

        if (isAtTop && firstVisibleItemPosition!! > expandableThreshold) expandView()
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    abstract fun onCollapseView()

    abstract fun onExpandView()

    private fun initItemLoading() {
        currentPage = startingPageIndex
        previousTotalItemCount = layoutManager.itemCount
        loading = layoutManager.itemCount == 0
    }

    private fun finishItemLoading() {
        loading = false
        previousTotalItemCount = layoutManager.itemCount
    }

    private fun loadMoreItem() {
        currentPage++
        onLoadMore(currentPage, layoutManager.itemCount)
        loading = true
    }

    private fun collapseView() {
        onCollapseView()
        isAtTop = true
    }

    private fun expandView() {
        onExpandView()
        isAtTop = false
    }
}
