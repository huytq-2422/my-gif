package com.sun.mygif.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.ui.base.BaseRecyclerViewAdapter
import com.sun.mygif.ui.base.BaseViewHolder
import com.sun.mygif.utils.screenWidth
import com.sun.mygif.widget.BASE_WIDTH
import kotlinx.android.synthetic.main.item_gif_vertical.view.*

class GifVerticalAdapter(
    private val onItemClick: (gif: Gif) -> Unit
) : BaseRecyclerViewAdapter<Gif, GifVerticalAdapter.VerticalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): VerticalViewHolder = VerticalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_gif_vertical, parent, false),
        onItemClick
    )

    inner class VerticalViewHolder(
        itemView: View,
        private val onItemClick: (gif: Gif) -> Unit
    ) : BaseViewHolder<Gif>(itemView) {

        override fun onBindData(gif: Gif) {
            super.onBindData(gif)

            itemView.gifItemVertical.apply {
                setRatioSize(
                    ratio = gif.width.toFloat() / gif.height,
                    size = screenWidth ushr 1,
                    base = BASE_WIDTH
                )
                gifUrl = gif.url
            }
        }

        override fun onHandleItemClick(mainItem: Gif) = onItemClick(mainItem)
    }
}
