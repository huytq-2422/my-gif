package com.sun.mygif.ui.detail

import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.GifDetail
import com.sun.mygif.data.model.GifResponse
import com.sun.mygif.data.model.GifsResponse
import com.sun.mygif.data.repository.GifLocalRepository
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.utils.CATEGORY_TRENDING

class DetailPresenter(
    private val detailView: DetailContract.View,
    private val localRepository: GifLocalRepository,
    private val remoteRepository: GifRemoteRepository
) : DetailContract.Presenter {

    override fun start() {
        detailView.showLoading()
    }

    override fun getGifDetail(gifId: String) {
        remoteRepository.getGif(gifId, object : OnDataLoadedCallback<GifResponse> {
            override fun onSuccess(data: GifResponse) {
                GifDetail(responseData = data.data).also {
                    detailView.run {
                        updateMainGifData(it)
                        showGifDetail(it)
                        showTitles(it)
                    }
                    getRelatedGifs(it.tags)
                }
                getFavoriteIcon(gifId)
            }

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }
        })
    }

    override fun handleFavorites(gifId: String) {
        localRepository.isFavorite(gifId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) = if (data) deleteFavorite(gifId) else addFavorite(gifId)

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }
        })
    }

    override fun shareToFacebook(url: String) = with(detailView) {
        notifySharing(R.drawable.ic_facebook_logo, R.string.notification_facebook_sharing)
        shareGifToFacebook(url)
    }

    override fun shareToMessenger(url: String) = with(detailView) {
        notifySharing(R.drawable.ic_messenger_logo, R.string.notification_messenger_sharing)
        shareGifToMessenger(url)
    }

    private fun addFavorite(gifId: String) {
        localRepository.addFavorite(gifId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                with(detailView) {
                    if (data) {
                        showLikeIcon(R.drawable.ic_like_black)
                        notifyExecuteSuccess(
                            R.drawable.ic_like_black,
                            R.string.notification_add_favorite_success
                        )
                    } else {
                        notifyExecuteFailed(
                            R.drawable.ic_sentiment_dissatisfied_black_24dp,
                            R.string.notification_add_favorite_success
                        )
                    }
                }
            }

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }

        })
    }

    private fun deleteFavorite(gifId: String) {
        localRepository.deleteFavorite(gifId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                with(detailView) {
                    if (data) {
                        showLikeIcon(R.drawable.ic_like_border_black)
                        notifyExecuteSuccess(
                            R.drawable.ic_thumb_down_black_24dp,
                            R.string.notification_delete_success
                        )
                    } else {
                        notifyExecuteFailed(
                            R.drawable.ic_sentiment_dissatisfied_black_24dp,
                            R.string.notification_delete_failed
                        )
                    }
                }
            }

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }

        })
    }

    private fun getFavoriteIcon(gifId: String) {
        localRepository.isFavorite(gifId, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) = detailView.showLikeIcon(
                if (data) R.drawable.ic_like_black else R.drawable.ic_like_border_black
            )

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }

        })
    }

    private fun getRelatedGifs(tags: List<String>) {
        val tag = if (tags.isEmpty()) CATEGORY_TRENDING else tags.random()
        if (tag == CATEGORY_TRENDING) getTrendingGifs(offset = 0) else getGifsByTag(tag = tag, offset = 0)
    }

    private fun getGifsByTag(tag: String, offset: Int) {
        remoteRepository.getSearches(tag, offset, object : OnDataLoadedCallback<GifsResponse> {
            override fun onSuccess(data: GifsResponse) =
                with(detailView) {
                    updateRelatedGifsData(relatedGifs = data.data.map { Gif(responseData = it) })
                    hideLoading()
                    showRelatedTitles()
                }

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }
        })
    }

    private fun getTrendingGifs(offset: Int) {
        remoteRepository.getTrendingGifs(offset, object : OnDataLoadedCallback<GifsResponse> {
            override fun onSuccess(data: GifsResponse) = with(detailView) {
                updateRelatedGifsData(relatedGifs = data.data.map { Gif(responseData = it) })
                hideLoading()
            }

            override fun onFailed(exception: Exception) = with(detailView) {
                notifyExceptionMessage(exception)
                hideLoading()
            }
        })
    }
}
