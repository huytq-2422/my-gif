package com.sun.mygif.ui.search

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.remote.GifRemoteDataSource
import com.sun.mygif.recyclerview.EndlessRecyclerViewScrollListener
import com.sun.mygif.recyclerview.SpeedyStaggeredGridLayoutManager
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.detail.DetailFragment
import com.sun.mygif.ui.home.GifVerticalAdapter
import com.sun.mygif.utils.DEFAULT_GIFS_LIMIT
import com.sun.mygif.utils.EXTRA_SEARCH_KEY
import kotlinx.android.synthetic.main.fragment_search_content.*

private const val SPAN_COUNT_LAYOUT_MANAGER = 2

class SearchContentFragment : BaseFragment(), SearchContentContract.View {
    override val layoutResource = R.layout.fragment_search_content

    private val speedyLayoutManager = SpeedyStaggeredGridLayoutManager(
        spanCount = SPAN_COUNT_LAYOUT_MANAGER,
        orientation = StaggeredGridLayoutManager.VERTICAL
    )

    private val gifAdapter = GifVerticalAdapter {
        addFragment(R.id.constraintMain, DetailFragment.newInstance(it.id), true)
    }

    private lateinit var searchKey: String
    private lateinit var searchPresenter: SearchContentContract.Presenter

    override fun initComponents() {
        initAdapter()
        initRecyclerView()
    }

    override fun initData() {
        searchPresenter = SearchPresenter(this, GifRemoteRepository(GifRemoteDataSource.getInstance()))
        searchKey = arguments?.getString(EXTRA_SEARCH_KEY).toString().also {
            searchPresenter.getSearches(it, 0)
        }
    }

    private fun initAdapter() {
        gifAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerSearches?.run {
                    if (itemCount > DEFAULT_GIFS_LIMIT) {
                        speedyLayoutManager.scrollToPosition(
                            this@run, null, positionStart + 5,
                            SpeedyStaggeredGridLayoutManager.SLOWLY
                        )
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        recyclerSearches?.run {
            layoutManager = speedyLayoutManager
            adapter = gifAdapter

            addOnScrollListener(object : EndlessRecyclerViewScrollListener(speedyLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    searchPresenter.getSearches(searchKey, totalItemsCount)
                    speedyLayoutManager.scrollToBottom(this@run)
                }

                override fun onExpandView() {
                }

                override fun onCollapseView() {
                }
            })
        }
    }

    override fun appendSearches(gifs: List<Gif>) = gifAdapter.insertData(gifs)

    override fun showSearchedResults() {
        recyclerSearches?.visibility = View.VISIBLE
    }

    override fun hideSearchedResults() {
        recyclerSearches?.visibility = View.GONE
    }

    override fun showLoading() {
        searchProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        searchProgressBar?.visibility = View.GONE
    }

    override fun toast(message: String) = toastMsg(message)

    override fun setPresenter(presenter: SearchContentContract.Presenter) {
        searchPresenter = presenter
    }

    companion object {
        @JvmStatic
        fun newInstance(searchKey: String) = SearchContentFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_SEARCH_KEY, searchKey) }
        }
    }
}
