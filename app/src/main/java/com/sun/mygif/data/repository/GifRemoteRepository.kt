package com.sun.mygif.data.repository

import com.sun.mygif.data.model.GifResponse
import com.sun.mygif.data.model.GifsResponse
import com.sun.mygif.data.model.RandomGifResponse
import com.sun.mygif.data.source.GifDataSource
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.remote.GifRemoteDataSource

class GifRemoteRepository(private val dataSource: GifRemoteDataSource) : GifDataSource.Remote {
    override fun getTrendingGifs(offset: Int, callback: OnDataLoadedCallback<GifsResponse>) =
        dataSource.getTrendingGifs(offset, callback)

    override fun getSearches(q: String, offset: Int, callback: OnDataLoadedCallback<GifsResponse>) =
        dataSource.getSearches(q, offset, callback)

    override fun getGif(gifId: String, callback: OnDataLoadedCallback<GifResponse>) =
        dataSource.getGif(gifId, callback)

    override fun getRandomGif(q: String, callback: OnDataLoadedCallback<RandomGifResponse>) =
        dataSource.getRandomGif(q, callback)

    override fun getRandomGifs(qList: List<String>, callback: OnDataLoadedCallback<List<RandomGifResponse?>>) =
        dataSource.getRandomGifs(qList, callback)

    override fun getGifs(gifIds: List<String>, callback: OnDataLoadedCallback<GifsResponse>) =
        dataSource.getGifs(gifIds, callback)

    companion object {
        private var sInstance: GifRemoteRepository? = null
        @JvmStatic
        fun getInstance(remoteDataSource: GifRemoteDataSource): GifRemoteRepository =
            sInstance ?: GifRemoteRepository(remoteDataSource).also { sInstance = it }
    }
}
