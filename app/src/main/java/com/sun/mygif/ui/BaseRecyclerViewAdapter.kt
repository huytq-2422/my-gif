package com.sun.mygif.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.sun.mygif.utils.DiffUtilCallback

abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    private val items = ArrayList<T>()

    override fun getItemCount(): Int = items.size

    protected fun getItem(position: Int): T? = if (position in 0 until itemCount) items[position] else null

    fun updateData(newItems: List<T>) = DiffUtil.calculateDiff(DiffUtilCallback(items, newItems)).also {
        items.apply {
            clear()
            addAll(newItems)
        }
    }.dispatchUpdatesTo(this)

    fun insertData(insertItems: List<T>) = with(items) {
        val firstPosition = size
        addAll(insertItems)
        val secondPosition = size
        notifyItemRangeInserted(firstPosition, secondPosition - 1)
    }
}
