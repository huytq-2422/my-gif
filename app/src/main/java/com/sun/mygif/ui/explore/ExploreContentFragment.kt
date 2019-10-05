package com.sun.mygif.ui.explore

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.ExploreTopic
import com.sun.mygif.data.repository.TopicRepository
import com.sun.mygif.data.source.local.TopicLocalDataSource
import com.sun.mygif.data.source.local.dao.TopicDAOImpl
import com.sun.mygif.data.source.local.dao.TrendingTopicDAOImpl
import com.sun.mygif.data.source.remote.TopicRemoteDataSource
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_explore_content.*

class ExploreContentFragment : BaseFragment(), ExploreContract.View {
    override val layoutResource = R.layout.fragment_explore_content

    private val exploreAdapter = ExploreAdapter {
        addFragment(R.id.constraintMain, SearchFragment.newInstance(it.title), true)
    }
    private lateinit var explorePresenter: ExploreContract.Presenter

    override fun initComponents() {
        initActionBar(getString(R.string.title_explore), R.drawable.ic_explore_black_24dp)
        initRecyclerView()
    }

    override fun initData() = initPresenter()

    override fun setPresenter(presenter: ExploreContract.Presenter) {
        explorePresenter = presenter
    }

    private fun initRecyclerView() {
        recyclerParentTopics?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = exploreAdapter
        }
    }

    private fun initPresenter() {
        activity?.let {
            val trendingTopicDAO = TrendingTopicDAOImpl.getInstance(
                sharedPreferences = it.getPreferences(Context.MODE_PRIVATE)
            )
            val topicDAO = TopicDAOImpl.getInstance(it)

            val topicRepository = TopicRepository.getInstance(
                localDataSource = TopicLocalDataSource.getInstance(trendingTopicDAO, topicDAO),
                remoteDataSource = TopicRemoteDataSource.getInstance()
            )
            explorePresenter = ExplorePresenter(this, topicRepository)
            explorePresenter.start()
        }
    }

    override fun showExploreTopics(exploreTopics: List<ExploreTopic>) = exploreAdapter.updateData(exploreTopics)

    override fun notifyExceptionMessage(exception: Exception) = toastMsg(exception.message.toString())

    override fun toast(message: String) = toastMsg(message)

    override fun showLoading() {
        exploreProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        exploreProgressBar?.visibility = View.GONE
    }
}
