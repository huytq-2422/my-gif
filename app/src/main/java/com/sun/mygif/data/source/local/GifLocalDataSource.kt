package com.sun.mygif.data.source.local

import com.sun.mygif.data.source.GifDataSource
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.data.source.local.base.LocalAsyncTask
import com.sun.mygif.data.source.local.base.LocalDataHandler
import com.sun.mygif.data.source.local.dao.GifInfoDAO
import com.sun.mygif.utils.EMPTY_PARAMS

class GifLocalDataSource private constructor(private val gifInfoDAO: GifInfoDAO) : GifDataSource.Local {

    override fun getFavoriteIds(callback: OnDataLoadedCallback<List<String>>) {
        LocalAsyncTask(object : LocalDataHandler<String, List<String>> {
            override fun execute(params: String): List<String> = gifInfoDAO.getAll()
        }, callback).execute(EMPTY_PARAMS)
    }

    override fun addFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<String, Boolean> {
            override fun execute(params: String): Boolean = gifInfoDAO.add(params)
        }, callback).execute(gifId)
    }

    override fun deleteFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<String, Boolean> {
            override fun execute(params: String): Boolean = gifInfoDAO.delete(params)
        }, callback).execute(gifId)
    }

    override fun isFavorite(gifId: String, callback: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<String, Boolean> {
            override fun execute(params: String): Boolean = gifInfoDAO.findFavorite(params)
        }, callback).execute(gifId)
    }

    companion object {
        private var sInstance: GifLocalDataSource? = null
        @JvmStatic
        fun getInstance(gifInfoDAO: GifInfoDAO): GifLocalDataSource =
            sInstance ?: GifLocalDataSource(gifInfoDAO).also { sInstance = it }
    }
}
