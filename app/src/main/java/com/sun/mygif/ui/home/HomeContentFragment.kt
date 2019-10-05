package com.sun.mygif.ui.home

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.Topic
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.repository.TopicRepository
import com.sun.mygif.data.source.local.TopicLocalDataSource
import com.sun.mygif.data.source.local.dao.TopicDAOImpl
import com.sun.mygif.data.source.local.dao.TrendingTopicDAOImpl
import com.sun.mygif.data.source.remote.GifRemoteDataSource
import com.sun.mygif.data.source.remote.TopicRemoteDataSource
import com.sun.mygif.recyclerview.EndlessRecyclerViewScrollListener
import com.sun.mygif.recyclerview.SpeedyStaggeredGridLayoutManager
import com.sun.mygif.recyclerview.SpeedyStaggeredGridLayoutManager.Companion.SLOWLY
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.detail.DetailFragment
import com.sun.mygif.ui.search.SearchFragment
import com.sun.mygif.utils.DEFAULT_GIFS_LIMIT
import kotlinx.android.synthetic.main.fragment_home_content.*
import kotlinx.android.synthetic.main.fragment_home_content.view.*

private const val TAG = "HomeContentFragment"

class HomeContentFragment : BaseFragment(), HomeContentContract.View {

    override val layoutResource = R.layout.fragment_home_content

    private val topicAdapter: TopicAdapter = TopicAdapter {
        addFragment(R.id.constraintMain, SearchFragment.newInstance(it.title), true)
    }
    private val gifAdapter: GifVerticalAdapter = GifVerticalAdapter {
        addFragment(R.id.constraintMain, DetailFragment.newInstance(it.id), true)
    }
    private val speedyLayoutManager = SpeedyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private lateinit var homePresenter: HomeContentPresenter

    override fun initComponents() {
        initActionBar(title = getString(R.string.title_home), iconId = R.drawable.ic_home_black_24dp)
        initTrendingTopics()
        initTrendingGifs()
        initBackToHeadButton()
    }

    private fun initTrendingTopics() = with(recyclerTrendingTopics) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = topicAdapter
    }

    private fun initTrendingGifs() = with(recyclerTrendingGifs) {
        layoutManager = speedyLayoutManager

        adapter = gifAdapter.apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = with(speedyLayoutManager) {
                    if (itemCount > DEFAULT_GIFS_LIMIT) {
                        view?.run { scrollToPosition(recyclerTrendingGifs, null, positionStart + 5, SLOWLY) }
                    }
                }
            })
        }

        addOnScrollListener(object : EndlessRecyclerViewScrollListener(speedyLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                homePresenter.getTrendingGifs(totalItemsCount)
                view?.run { speedyLayoutManager.scrollToBottom(recyclerTrendingGifs) }
            }

            override fun onExpandView() = expandTrendingGifs()
            override fun onCollapseView() = collapseTrendingGifs()
        })
    }

    private fun initBackToHeadButton() = with(buttonScrollToTop) {
        setOnClickListener {
            view?.run { speedyLayoutManager.scrollToTop(recyclerTrendingGifs) }
        }
        hide()
    }

    override fun initData() {
        activity?.let {
            val trendingTopicDAO = TrendingTopicDAOImpl.getInstance(
                sharedPreferences = it.getPreferences(Context.MODE_PRIVATE)
            )
            val topicDAO = TopicDAOImpl.getInstance(it)

            val topicRepository = TopicRepository.getInstance(
                localDataSource = TopicLocalDataSource.getInstance(trendingTopicDAO, topicDAO),
                remoteDataSource = TopicRemoteDataSource.getInstance()
            )

            val gifRepository = GifRemoteRepository.getInstance(
                remoteDataSource = GifRemoteDataSource.getInstance()
            )
            homePresenter = HomeContentPresenter(this, topicRepository, gifRepository)
        }
        homePresenter.start()
    }

    override fun appendTrendingTopics(topics: List<Topic>) = topicAdapter.insertData(topics)

    override fun appendTrendingGifs(gifs: List<Gif>) = gifAdapter.insertData(gifs)

    override fun expandTrendingGifs() {
        view?.run {
            titleTrendingTopics.visibility = View.GONE
            recyclerTrendingTopics.visibility = View.GONE
            buttonScrollToTop.show()
        }
    }

    override fun collapseTrendingGifs() {
        view?.run {
            titleTrendingTopics.visibility = View.VISIBLE
            recyclerTrendingTopics.visibility = View.VISIBLE
            buttonScrollToTop.hide()
        }
    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun toast(message: String) = toastMsg(message)

    override fun setPresenter(presenter: HomeContentContract.Presenter) {
        homePresenter = presenter as HomeContentPresenter
    }
}
