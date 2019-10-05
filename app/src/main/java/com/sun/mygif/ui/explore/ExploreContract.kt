package com.sun.mygif.ui.explore

import com.sun.mygif.data.model.ExploreTopic
import com.sun.mygif.ui.base.BasePresenter
import com.sun.mygif.ui.base.BaseView

interface ExploreContract {
    interface View : BaseView<Presenter> {
        fun showExploreTopics(exploreTopics: List<ExploreTopic>)
        fun notifyExceptionMessage(exception: Exception)
    }

    interface Presenter : BasePresenter
}
