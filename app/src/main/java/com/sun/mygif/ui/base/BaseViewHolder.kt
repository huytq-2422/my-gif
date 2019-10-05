package com.sun.mygif.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View

open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var itemData: T? = null
    protected var itemPosition: Int = -1

    init {
        itemView.setOnClickListener {
            itemData?.let { onHandleItemClick(it) }
        }
    }

    open fun onBindData(itemData: T) {
        this.itemData = itemData
    }

    open fun onBindData(itemPosition: Int, itemData: T) {
        this.itemPosition = itemPosition
        this.itemData = itemData
    }

    open fun onHandleItemClick(mainItem: T) {
    }
}
