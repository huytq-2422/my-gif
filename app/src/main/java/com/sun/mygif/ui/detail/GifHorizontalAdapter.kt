package com.sun.mygif.ui.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.mygif.R
import com.sun.mygif.data.model.Gif
import com.sun.mygif.ui.base.BaseRecyclerViewAdapter
import com.sun.mygif.ui.base.BaseViewHolder
import com.sun.mygif.widget.BASE_HEIGHT
import kotlinx.android.synthetic.main.item_gif_horizontal.view.*

private const val GIF_SIZE = 150f

class GifHorizontalAdapter(
    private val onItemClick: (gif: Gif) -> Unit
) : BaseRecyclerViewAdapter<Gif, GifHorizontalAdapter.HorizontalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int) = HorizontalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_gif_horizontal, parent, false),
        onItemClick
    )

    class HorizontalViewHolder(
        itemView: View,
        private val onItemClick: (gif: Gif) -> Unit
    ) : BaseViewHolder<Gif>(itemView) {

        override fun onBindData(gif: Gif) {
            super.onBindData(gif)

            itemView.gifItemHorizontal?.run {
                setRatioSize(
                    ratio = gif.width.toFloat() / gif.height,
                    size = getPxFromDip(GIF_SIZE).toInt(),
                    base = BASE_HEIGHT
                )
                gifUrl = gif.url
            }
        }

        override fun onHandleItemClick(mainItem: Gif) = onItemClick(mainItem)

        private fun getPxFromDip(value: Float): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, itemView.context.resources.displayMetrics)
    }
}
