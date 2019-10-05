package com.sun.mygif.ui.favorite

import com.sun.mygif.data.model.GifFavorite
import com.sun.mygif.ui.base.BasePresenter
import com.sun.mygif.ui.base.BaseView

interface FavoriteContentContract {
    interface View : BaseView<Presenter> {
        fun showFavorites(favorites: List<GifFavorite>)
        fun showEmptyResultNotification()
        fun hideEmptyResultNotification()
        fun updateFavoritesAfterRemovingItem(position: Int)
        fun notifyDeleteSuccess()
        fun notifyDeleteFailed()
        fun notifyExceptionMessage(exception: Exception)
    }

    interface Presenter : BasePresenter {
        fun deleteFavorite(position: Int, gifId: String)
    }
}
