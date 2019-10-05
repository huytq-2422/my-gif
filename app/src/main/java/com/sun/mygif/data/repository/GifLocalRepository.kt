package com.sun.mygif.data.repository

import com.sun.mygif.data.source.GifDataSource
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.local.GifLocalDataSource

class GifLocalRepository(private val dataSource: GifLocalDataSource) : GifDataSource.Local {
    override fun getFavoriteIds(callback: OnDataLoadedCallback<List<String>>) =
        dataSource.getFavoriteIds(callback)

    override fun addFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) =
        dataSource.addFavorite(gifId, callback)

    override fun deleteFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) =
        dataSource.deleteFavorite(gifId, callback)

    override fun isFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) =
        dataSource.isFavorite(gifId, callback)

    companion object {
        private var sInstance: GifLocalRepository? = null
        @JvmStatic
        fun getInstance(localDataSource: GifLocalDataSource): GifLocalRepository =
            sInstance ?: GifLocalRepository(localDataSource).also { sInstance = it }
    }
}
