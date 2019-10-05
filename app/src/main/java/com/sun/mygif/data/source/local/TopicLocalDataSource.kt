package com.sun.mygif.data.source.local

import com.sun.mygif.data.model.ParentTopic
import com.sun.mygif.data.model.Topic
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.TopicDataSource
import com.sun.mygif.data.source.local.base.LocalAsyncTask
import com.sun.mygif.data.source.local.base.LocalDataHandler
import com.sun.mygif.data.source.local.dao.TopicDAO
import com.sun.mygif.data.source.local.dao.TrendingTopicDAO
import com.sun.mygif.utils.EMPTY_PARAMS

class TopicLocalDataSource private constructor(
    private val trendingTopicDAO: TrendingTopicDAO,
    private val topicDAO: TopicDAO
) : TopicDataSource.Local {

    override fun getSavedTopicsByTitles(titles: List<String>, callback: OnDataLoadedCallback<List<Topic?>>){
        LocalAsyncTask(object : LocalDataHandler<List<String>, List<Topic?>> {
            override fun execute(params: List<String>): List<Topic?> = trendingTopicDAO.getSavedTopicsByTitles(params)
        }, callback).execute(titles)
    }

    override fun saveTopic(topic: Topic) = trendingTopicDAO.saveTopic(topic)

    override fun getParentTopics(callback: OnDataLoadedCallback<List<ParentTopic>>) {
        LocalAsyncTask(object : LocalDataHandler<String, List<ParentTopic>> {
            override fun execute(params: String): List<ParentTopic> = topicDAO.getParentTopics()
        }, callback).execute(EMPTY_PARAMS)
    }

    override fun getSubTopics(callback: OnDataLoadedCallback<List<Topic>>) {
        LocalAsyncTask(object : LocalDataHandler<String, List<Topic>> {
            override fun execute(params: String): List<Topic> = topicDAO.getSubTopics()
        }, callback).execute(EMPTY_PARAMS)
    }

    companion object {
        private var sInstance: TopicLocalDataSource? = null
        @JvmStatic
        fun getInstance(trendingTopicDAO: TrendingTopicDAO, topicDAO: TopicDAO): TopicLocalDataSource =
            sInstance ?: TopicLocalDataSource(trendingTopicDAO, topicDAO).also { sInstance = it }
    }
}
