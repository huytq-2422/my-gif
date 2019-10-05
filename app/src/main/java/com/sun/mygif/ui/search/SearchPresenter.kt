package com.sun.mygif.ui.search

import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.GifsResponse
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.OnDataLoadedCallback

class SearchPresenter(
    private val searchView: SearchContentContract.View,
    private val gifRepository: GifRemoteRepository
) : SearchContentContract.Presenter {

    override fun start() {
    }

    override fun getSearches(title: String, offset: Int) = with(searchView) {
        showLoading()
        gifRepository.getSearches(title, offset, object : OnDataLoadedCallback<GifsResponse> {
            override fun onSuccess(data: GifsResponse) {
                val gifs = data.data.map { Gif(responseData = it) }

                hideLoading()
                if (gifs.isNotEmpty()) {
                    showSearchedResults()
                    appendSearches(gifs)
                } else if (offset == 0) hideSearchedResults()
            }

            override fun onFailed(exception: Exception) {
                toast(exception.message.toString())
                hideLoading()
                hideSearchedResults()
            }
        })
    }
}
