package com.sun.mygif.ui.search

import com.sun.mygif.data.model.Gif
import com.sun.mygif.ui.base.BasePresenter
import com.sun.mygif.ui.base.BaseView

interface SearchContentContract {
    interface View : BaseView<Presenter> {
        fun appendSearches(gifs: List<Gif>)
        fun showSearchedResults()
        fun hideSearchedResults()
    }

    interface Presenter : BasePresenter {
        fun getSearches(title: String, offset: Int)
    }
}
