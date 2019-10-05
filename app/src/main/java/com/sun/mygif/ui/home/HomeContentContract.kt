package com.sun.mygif.ui.home

import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.Topic
import com.sun.mygif.ui.base.BasePresenter
import com.sun.mygif.ui.base.BaseView

interface HomeContentContract {
    interface View : BaseView<Presenter> {
        fun appendTrendingTopics(topics: List<Topic>)
        fun appendTrendingGifs(gifs: List<Gif>)
        fun expandTrendingGifs()
        fun collapseTrendingGifs()
    }

    interface Presenter : BasePresenter {
        fun getTrendingTopics()
        fun getTrendingGifs(offset: Int)
    }
}
