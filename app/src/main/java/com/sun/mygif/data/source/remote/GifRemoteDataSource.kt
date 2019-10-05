package com.sun.mygif.data.source.remote

import com.sun.mygif.BuildConfig
import com.sun.mygif.data.model.DataRequest
import com.sun.mygif.data.model.GifResponse
import com.sun.mygif.data.model.GifsResponse
import com.sun.mygif.data.model.RandomGifResponse
import com.sun.mygif.data.source.GifDataSource
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.remote.response.*
import com.sun.mygif.utils.*

class GifRemoteDataSource private constructor() : GifDataSource.Remote {
    override fun getTrendingGifs(offset: Int, callback: OnDataLoadedCallback<GifsResponse>) {
        GetResponseAsync(GifsResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTP,
                authority = AUTHORITY_GIPHY,
                paths = listOf(PATH_V1, PATH_GIFS, PATH_TRENDING),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.GIPHY_API_KEY,
                    QUERY_LIMIT to DEFAULT_GIFS_LIMIT,
                    QUERY_OFFSET to offset
                )
            ).toUrl()
        )
    }

    override fun getSearches(q: String, offset: Int, callback: OnDataLoadedCallback<GifsResponse>) {
        GetResponseAsync(GifsResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTP,
                authority = AUTHORITY_GIPHY,
                paths = listOf(PATH_V1, PATH_GIFS, PATH_SEARCH),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.GIPHY_API_KEY,
                    QUERY_Q to q,
                    QUERY_LIMIT to DEFAULT_GIFS_LIMIT,
                    QUERY_OFFSET to offset
                )
            ).toUrl()
        )
    }

    override fun getGif(gifId: String, callback: OnDataLoadedCallback<GifResponse>) {
        GetResponseAsync(GifResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTP,
                authority = AUTHORITY_GIPHY,
                paths = listOf(PATH_V1, PATH_GIFS, gifId),
                queryParams = mapOf(QUERY_API_KEY to BuildConfig.GIPHY_API_KEY2)
            ).toUrl()
        )
    }

    override fun getRandomGif(q: String, callback: OnDataLoadedCallback<RandomGifResponse>) {
        GetResponseAsync(RandomGifResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTPS,
                authority = AUTHORITY_TENOR,
                paths = listOf(PATH_V1, PATH_RANDOM),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.TENOR_API_KEY,
                    QUERY_Q to q,
                    QUERY_LIMIT to DEFAULT_RANDOM_LIMIT,
                    QUERY_LOCALE to DEFAULT_LOCALE
                )
            ).toUrl()
        )
    }

    override fun getRandomGifs(qList: List<String>, callback: OnDataLoadedCallback<List<RandomGifResponse?>>) {
        val urlRequests = qList.map {
            DataRequest(
                scheme = SCHEME_HTTPS,
                authority = AUTHORITY_TENOR,
                paths = listOf(PATH_V1, PATH_RANDOM),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.TENOR_API_KEY,
                    QUERY_Q to it,
                    QUERY_LIMIT to DEFAULT_RANDOM_LIMIT,
                    QUERY_LOCALE to DEFAULT_LOCALE
                )
            ).toUrl()
        }
        GetResponsesAsync(RandomGifResponseHandler(), callback).execute(urlRequests)
    }

    override fun getGifs(gifIds: List<String>, callback: OnDataLoadedCallback<GifsResponse>) {
        GetResponseAsync(GifsResponseHandler(), callback).execute(
            DataRequest(
                scheme = SCHEME_HTTP,
                authority = AUTHORITY_GIPHY,
                paths = listOf(PATH_V1, PATH_GIFS),
                queryParams = mapOf(
                    QUERY_API_KEY to BuildConfig.GIPHY_API_KEY,
                    QUERY_IDS to gifIds.joinToString()
                )
            ).toUrl()
        )
    }

    companion object {
        private var sInstance: GifRemoteDataSource? = null
        @JvmStatic
        fun getInstance(): GifRemoteDataSource {
            return sInstance ?: GifRemoteDataSource().also { sInstance = it }
        }
    }
}
