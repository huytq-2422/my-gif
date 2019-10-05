package com.sun.mygif.ui.favorite

import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.GifFavorite
import com.sun.mygif.data.model.GifsResponse
import com.sun.mygif.data.repository.GifLocalRepository
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.OnDataLoadedCallback

private const val TAG = "FavoritePresenter"

class FavoriteContentPresenter(
    private val favoriteView: FavoriteContentContract.View,
    private val localRepository: GifLocalRepository,
    private val remoteRepository: GifRemoteRepository
) : FavoriteContentContract.Presenter {

    init {
        favoriteView.setPresenter(this)
    }

    override fun start() {
        favoriteView.showLoading()
        getFavoriteIds()
    }

    override fun deleteFavorite(position: Int, gifId: String) = with(favoriteView) {
        localRepository.deleteFavorite(gifId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(isDeleteSuccess: Boolean) {
                if (isDeleteSuccess) {
                    updateFavoritesAfterRemovingItem(position)
                    notifyDeleteSuccess()
                } else {
                    notifyDeleteFailed()
                }
            }

            override fun onFailed(exception: Exception) = notifyExceptionMessage(exception)
        })
    }

    private fun getFavoriteIds() = with(favoriteView) {
        localRepository.getFavoriteIds(object : OnDataLoadedCallback<List<String>> {
            override fun onSuccess(data: List<String>) {
                if (data.isNotEmpty()) {
                    getFavoriteGifs(gifIds = data)
                } else {
                    hideLoading()
                    showEmptyResultNotification()
                }
            }

            override fun onFailed(exception: Exception) {
                hideLoading()
                notifyExceptionMessage(exception)
            }
        })
    }

    private fun getFavoriteGifs(gifIds: List<String>) = with(favoriteView) {
        remoteRepository.getGifs(gifIds, object : OnDataLoadedCallback<GifsResponse> {
            override fun onSuccess(data: GifsResponse) {
                val favorites = data.data.map {
                    GifFavorite(title = it.title, gif = Gif(it))
                }
                showFavorites(favorites)
                hideLoading()
            }

            override fun onFailed(exception: Exception) {
                hideLoading()
                notifyExceptionMessage(exception)
            }
        })
    }
}
