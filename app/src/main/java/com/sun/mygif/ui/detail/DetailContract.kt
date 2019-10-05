package com.sun.mygif.ui.detail

import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.GifDetail
import com.sun.mygif.ui.base.BasePresenter
import com.sun.mygif.ui.base.BaseView

interface DetailContract {
    interface View : BaseView<Presenter> {
        fun updateMainGifData(gifDetail: GifDetail)
        fun showGifDetail(gifDetail: GifDetail)
        fun showTitles(gifDetail: GifDetail)
        fun showLikeIcon(resId: Int)
        fun updateRelatedGifsData(relatedGifs: List<Gif>)
        fun showRelatedTitles()
        fun notifyExceptionMessage(exception: Exception)
        fun notifyExecuteSuccess(iconId: Int, msgId: Int)
        fun notifyExecuteFailed(iconId: Int, msgId: Int)
        fun notifySharing(iconId: Int, msgId: Int)
        fun shareGifToFacebook(gifUrl: String)
        fun shareGifToMessenger(gifUrl: String)
    }

    interface Presenter : BasePresenter {
        fun getGifDetail(gifId: String)
        fun handleFavorites(gifId: String)
        fun shareToFacebook(url: String)
        fun shareToMessenger(url: String)
    }
}
