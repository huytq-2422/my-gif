package com.sun.mygif.data.source.remote

import com.sun.mygif.BuildConfig
import com.sun.mygif.data.model.DataRequest
import com.sun.mygif.data.model.TopicsResponse
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.TopicDataSource
import com.sun.mygif.data.source.remote.response.GetResponseAsync
import com.sun.mygif.data.source.remote.response.TopicsResponseHandler
import com.sun.mygif.utils.*

class TopicRemoteDataSource private constructor() : TopicDataSource.Remote {

    override fun getUpdatedTrendingTopics(callback: OnDataLoadedCallback<TopicsResponse>) {
        GetResponseAsync(TopicsResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTPS,
                authority = AUTHORITY_TENOR,
                paths = listOf(PATH_V1, PATH_TRENDING_TERMS),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.TENOR_API_KEY,
                    QUERY_LIMIT to DEFAULT_TOPIC_LIMIT,
                    QUERY_LOCALE to DEFAULT_LOCALE
                )
            ).toUrl()
        )
    }

    companion object {
        private var sInstance: TopicRemoteDataSource? = null
        @JvmStatic
        fun getInstance(): TopicRemoteDataSource {
            return sInstance ?: TopicRemoteDataSource().also { sInstance = it }
        }
    }
}
