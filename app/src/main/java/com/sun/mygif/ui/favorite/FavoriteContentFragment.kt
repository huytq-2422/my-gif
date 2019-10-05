package com.sun.mygif.ui.favorite

import android.app.AlertDialog
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.GifFavorite
import com.sun.mygif.data.repository.GifLocalRepository
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.local.GifLocalDataSource
import com.sun.mygif.data.source.local.dao.GifInfoDAOImpl
import com.sun.mygif.data.source.remote.GifRemoteDataSource
import com.sun.mygif.recyclerview.SpeedyStaggeredGridLayoutManager
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.detail.DetailFragment
import kotlinx.android.synthetic.main.fragment_favorite_content.*

private const val SPAN_COUNT_LAYOUT_MANAGER = 2

class FavoriteContentFragment : BaseFragment(), FavoriteContentContract.View {

    override val layoutResource = R.layout.fragment_favorite_content

    private lateinit var favoritePresenter: FavoriteContentContract.Presenter

    private val favoriteAdapter = FavoriteAdapter(
        onItemClick = { favorite ->
            addFragment(R.id.constraintMain, DetailFragment.newInstance(favorite.gif.id), addToBackStack = true)
        },
        onItemDelete = { index, favorite -> showRemoveDialog(index, favorite) }
    )
    private val speedyLayoutManager = SpeedyStaggeredGridLayoutManager(
        spanCount = SPAN_COUNT_LAYOUT_MANAGER,
        orientation = StaggeredGridLayoutManager.VERTICAL
    )

    override fun initComponents() {
        initActionBar(getString(R.string.title_favorite), R.drawable.ic_favorite_black_24dp)
        initRecyclerView()
    }

    override fun initData() {
        context?.let {
            val gifInfoDAO = GifInfoDAOImpl.getInstance(it)
            val remoteRepository = GifRemoteRepository.getInstance(
                remoteDataSource = GifRemoteDataSource.getInstance()
            )
            val localRepository: GifLocalRepository = GifLocalRepository.getInstance(
                localDataSource = GifLocalDataSource.getInstance(gifInfoDAO)
            )
            favoritePresenter = FavoriteContentPresenter(this, localRepository, remoteRepository)
            favoritePresenter.start()
        }
    }

    private fun initRecyclerView() = recyclerFavorites?.run {
        adapter = favoriteAdapter
        layoutManager = speedyLayoutManager
    }

    override fun setPresenter(presenter: FavoriteContentContract.Presenter) {
        favoritePresenter = presenter
    }

    override fun showLoading() {
        favoriteProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        favoriteProgressBar?.visibility = View.GONE
    }

    override fun showFavorites(favorites: List<GifFavorite>) =
        if (favorites.isEmpty()) {
            showEmptyResultNotification()
        } else {
            hideEmptyResultNotification()
            favoriteAdapter.updateData(favorites)
        }

    override fun showEmptyResultNotification() {
        recyclerFavorites?.visibility = View.GONE
    }

    override fun hideEmptyResultNotification() {
        recyclerFavorites?.visibility = View.VISIBLE
    }

    override fun updateFavoritesAfterRemovingItem(position: Int) {
        favoriteAdapter.removeData(position)
        if (favoriteAdapter.itemCount == 0) {
            showEmptyResultNotification()
        }
    }

    override fun notifyDeleteSuccess() = toastMsg(R.string.notification_delete_success)

    override fun notifyDeleteFailed() = toastMsg(R.string.notification_delete_failed)

    override fun notifyExceptionMessage(exception: Exception) = toastMsg(exception.message.toString())

    override fun toast(message: String) = toastMsg(message)

    private fun showRemoveDialog(position: Int, gifFavorite: GifFavorite) = with(AlertDialog.Builder(context)) {
        setIcon(R.drawable.ic_delete_forever_black_24dp)
        setTitle(String.format(context.getString(R.string.format_title_remove_favorite_item), gifFavorite.title))
        setMessage(context.getString(R.string.message_remove_from_favorites))

        setPositiveButton(context.getString(R.string.title_yes)) { _, _ ->
            favoritePresenter.deleteFavorite(position, gifFavorite.gif.id)
        }
        setNegativeButton(context.getString(R.string.title_no)) { _, _ ->
        }
        show()
    }
}
