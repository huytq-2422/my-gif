package com.sun.mygif.ui.viewer

import android.content.Context
import android.content.Intent
import android.view.View
import com.sun.mygif.R
import com.sun.mygif.data.model.GifDetail
import com.sun.mygif.ui.base.BaseActivity
import com.sun.mygif.utils.EXTRA_GIF_DETAIL
import com.sun.mygif.utils.screenHeight
import com.sun.mygif.utils.screenWidth
import com.sun.mygif.widget.BASE_HEIGHT
import com.sun.mygif.widget.BASE_WIDTH
import kotlinx.android.synthetic.main.activity_view.*

class ViewerActivity : BaseActivity(), View.OnClickListener {
    override val layoutResource: Int = R.layout.activity_view

    private val scaledWidth = screenWidth * 0.9
    private val scaledHeight = screenHeight * 0.8
    private val scaledRatio = (scaledWidth / scaledHeight).toFloat()

    override fun initComponent() {
        titleView?.isSelected = true
        backView.setOnClickListener(this)
    }

    override fun initData() {
        val gifDetail = intent?.getParcelableExtra(EXTRA_GIF_DETAIL) as GifDetail

        titleView?.text = gifDetail.title

        val ratio = gifDetail.gif.width.toFloat() / gifDetail.gif.height
        val size = if (ratio > scaledRatio) scaledWidth else scaledHeight
        val base = if (ratio > scaledRatio) BASE_WIDTH else BASE_HEIGHT

        gifView?.run {
            setRatioSize(ratio, size.toInt(), base)
            gifUrl = gifDetail.gif.url
        }
    }

    override fun onClick(v: View?) = onBackPressed()

    companion object {
        @JvmStatic
        fun getIntent(context: Context, gifDetail: GifDetail) = Intent(context, ViewerActivity::class.java).apply {
            putExtra(EXTRA_GIF_DETAIL, gifDetail)
        }
    }
}
