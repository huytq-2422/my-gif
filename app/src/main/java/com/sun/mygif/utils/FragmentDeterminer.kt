package com.sun.mygif.utils

import android.support.v4.app.Fragment
import com.sun.mygif.ui.detail.DetailFragment
import com.sun.mygif.ui.explore.ExploreContentFragment
import com.sun.mygif.ui.favorite.FavoriteContentFragment
import com.sun.mygif.ui.home.HomeContentFragment
import com.sun.mygif.ui.search.SearchContentFragment
import com.sun.mygif.ui.search.SearchFragment

fun Fragment.isFirstScreen() =
    (this is HomeContentFragment) or (this is FavoriteContentFragment) or (this is ExploreContentFragment)

fun Fragment.isSearchScreen() =
    (this is SearchContentFragment) or (this is SearchContentFragment) or (this is SearchFragment)

fun Fragment.isShowingDetailScreen() = (this is DetailFragment)
