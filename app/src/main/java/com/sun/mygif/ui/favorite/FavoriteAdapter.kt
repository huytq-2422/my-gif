package com.sun.mygif.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.mygif.R
import com.sun.mygif.data.model.GifFavorite
import com.sun.mygif.ui.base.BaseRecyclerViewAdapter
import com.sun.mygif.ui.base.BaseViewHolder
import com.sun.mygif.utils.screenWidth
import com.sun.mygif.widget.BASE_WIDTH
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(
    private val onItemClick: (gifFavorite: GifFavorite) -> Unit,
    private val onItemDelete: (position: Int, gifFavorite: GifFavorite) -> Unit
) : BaseRecyclerViewAdapter<GifFavorite, FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FavoriteViewHolder = FavoriteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false),
        onItemClick,
        onItemDelete
    )

    override fun onBindViewHolder(viewHolder: FavoriteViewHolder, position: Int) {
        getItem(position)?.let {
            viewHolder.onBindData(position, it)
        }
    }

    class FavoriteViewHolder(
        itemView: View,
        private val onItemClick: (gifFavorite: GifFavorite) -> Unit,
        private val onItemDelete: (position: Int, gifFavorite: GifFavorite) -> Unit
    ) : BaseViewHolder<GifFavorite>(itemView) {

        init {
            with(itemView) {
                gifItemFavorite.setOnClickListener {
                    itemData?.let { favorite -> onItemClick(favorite) }
                }
                iconDeleteItemFavorite.setOnClickListener {
                    itemData?.let { favorite -> onItemDelete(itemPosition, favorite) }
                }
                titleItemFavorite?.run {
                    width = screenWidth ushr 1
                    isSelected = true
                }
            }
        }

        override fun onBindData(itemPosition: Int, itemData: GifFavorite) {
            super.onBindData(itemPosition, itemData)

            itemView.gifItemFavorite?.run {
                setRatioSize(
                    ratio = itemData.gif.width.toFloat() / itemData.gif.height,
                    size = screenWidth ushr 1,
                    base = BASE_WIDTH
                )
                gifUrl = itemData.gif.url
            }
            itemView.titleItemFavorite?.text = itemData.title
        }
    }
}
