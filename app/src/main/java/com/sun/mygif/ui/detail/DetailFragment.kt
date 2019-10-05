package com.sun.mygif.ui.detail

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.PopupMenu
import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.data.model.GifDetail
import com.sun.mygif.data.repository.GifLocalRepository
import com.sun.mygif.data.repository.GifRemoteRepository
import com.sun.mygif.data.source.local.GifLocalDataSource
import com.sun.mygif.data.source.local.dao.GifInfoDAOImpl
import com.sun.mygif.data.source.remote.GifRemoteDataSource
import com.sun.mygif.ui.base.BaseFragment
import com.sun.mygif.ui.base.OnFragmentInteractionListener
import com.sun.mygif.ui.explore.ExploreContentFragment
import com.sun.mygif.ui.favorite.FavoriteContentFragment
import com.sun.mygif.ui.home.HomeContentFragment
import com.sun.mygif.ui.viewer.ViewerActivity
import com.sun.mygif.utils.*
import com.sun.mygif.widget.BASE_WIDTH
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : BaseFragment(), DetailContract.View, View.OnClickListener {

    override val layoutResource = R.layout.fragment_detail

    private val gifAdapter = GifHorizontalAdapter {
        addFragment(R.id.constraintMain, newInstance(it.id), true)
    }

    private lateinit var mainGifId: String
    private lateinit var mainGif: GifDetail
    private lateinit var detailPresenter: DetailContract.Presenter
    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun initComponents() {
        initTitles()
        initRecyclerView()
        handleEventClick()
    }

    override fun initData() {
        mainGifId = arguments?.getString(EXTRA_GIF_ID) ?: GIF_ID_DEFAULT
        initPresenter()
    }

    private fun initTitles() {
        titleGifDetail?.isSelected = true
        titleSource?.isSelected = true
    }

    private fun initRecyclerView() {
        recyclerRelatedGifs?.run {
            adapter = gifAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun handleEventClick() {
        backDetail?.setOnClickListener(this)
        gifViewDetail?.setOnClickListener(this)
        imageLike?.setOnClickListener(this)
        imageCopy?.setOnClickListener(this)
        imageMessenger?.setOnClickListener(this)
        imageFacebook?.setOnClickListener(this)
        gifHead?.setOnClickListener(this)
        buttonTabChange?.setOnClickListener(this)
        textRelated?.setOnClickListener(this)
    }

    private fun initPresenter() {
        context?.let {
            val gifInfoDAO = GifInfoDAOImpl.getInstance(it)
            val remoteRepository = GifRemoteRepository.getInstance(
                remoteDataSource = GifRemoteDataSource.getInstance()
            )
            val localRepository: GifLocalRepository = GifLocalRepository.getInstance(
                localDataSource = GifLocalDataSource.getInstance(gifInfoDAO)
            )
            detailPresenter = DetailPresenter(this, localRepository, remoteRepository)
            detailPresenter.run {
                start()
                getGifDetail(mainGifId)
            }
        }
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        detailPresenter = presenter
    }

    override fun updateMainGifData(gifDetail: GifDetail) {
        mainGif = gifDetail
    }

    override fun showGifDetail(gifDetail: GifDetail) {
        gifViewDetail?.run {
            setRatioSize(
                ratio = mainGif.gif.width.toFloat() / mainGif.gif.height,
                size = (screenWidth * 0.8).toInt(),
                base = BASE_WIDTH
            )
            gifUrl = mainGif.gif.url
        }
    }

    override fun showTitles(gifDetail: GifDetail) = with(gifDetail) {
        titleGifDetail?.text = if (title.isEmpty()) getString(R.string.title_default) else title
        titleSource?.text = if (source.isEmpty()) getString(R.string.title_source_giphy) else source
    }

    override fun showLikeIcon(resId: Int) {
        imageLike?.setImageResource(resId)
    }

    override fun updateRelatedGifsData(relatedGifs: List<Gif>) = gifAdapter.updateData(relatedGifs)

    override fun showRelatedTitles() {
        textRelated?.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backDetail -> backToPreviousFragment()

            R.id.gifViewDetail -> showOnlyGifView()

            R.id.buttonTabChange -> showPopupMenu()

            R.id.imageLike -> if (::mainGif.isInitialized) {
                detailPresenter.handleFavorites(gifId = mainGifId)
            }

            R.id.imageCopy -> if (::mainGif.isInitialized) {
                copyGifUrl(mainGif)
            }

            R.id.imageFacebook -> if (::mainGif.isInitialized) {
                detailPresenter.shareToFacebook(url = mainGif.gif.url)
            }

            R.id.imageMessenger -> if (::mainGif.isInitialized) {
                detailPresenter.shareToMessenger(url = mainGif.gif.url)
            }

            R.id.textRelated -> handleRelatedGifsDisplaying()

            R.id.gifHead -> showGifHead()
        }
    }

    private fun showOnlyGifView() = activity?.let {
        if (::mainGif.isInitialized) {
            startActivity(ViewerActivity.getIntent(it, mainGif))
        }
    }

    private fun handleRelatedGifsDisplaying() {
        recyclerRelatedGifs?.run {
            visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun copyGifUrl(gifDetail: GifDetail) {
        context?.run {
            ClipboardHelper.copyText(this, gifDetail.title, gifDetail.gif.url)
            toastMsg(R.drawable.ic_content_copy_black_24dp, R.string.notification_text_copied_to_clipboard)
        }
    }

    private fun showPopupMenu() {
        activity?.let { activity ->
            PopupMenu(activity, buttonTabChange).apply {
                menuInflater.inflate(R.menu.menu_bottom_navigation, menu)

                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.item_home -> listener?.onTabChange(menuItem.itemId, HomeContentFragment())
                        R.id.item_explore -> listener?.onTabChange(menuItem.itemId, ExploreContentFragment())
                        R.id.item_favorite -> listener?.onTabChange(menuItem.itemId, FavoriteContentFragment())
                    }
                    true
                }
            }.show()
        }
    }

    override fun shareGifToFacebook(gifUrl: String) {
        activity?.run {
            ShareHelper.shareLinkToFacebook(this, gifUrl)
        }
    }

    override fun shareGifToMessenger(gifUrl: String) {
        activity?.run {
            ShareHelper.shareLinkToMessenger(this, gifUrl)
        }
    }

    private fun showGifHead() {
        listener?.onSaveState(mainGifId)
        if (::mainGif.isInitialized) {
            copyGifUrl(mainGif)
            openGifHead(mainGif.gifSmall)
        }
    }

    override fun notifyExecuteSuccess(iconId: Int, msgId: Int) = toastMsg(iconId, msgId)
    override fun notifyExecuteFailed(iconId: Int, msgId: Int) = toastMsg(iconId, msgId)
    override fun notifySharing(iconId: Int, msgId: Int) = toastMsg(iconId, msgId)
    override fun notifyExceptionMessage(exception: Exception) = toastMsg(exception.message.toString())

    override fun toast(message: String) = toastMsg(message)

    override fun showLoading() {
        detailProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        detailProgressBar?.visibility = View.GONE
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(gifId: String) = DetailFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_GIF_ID, gifId) }
        }
    }
}
