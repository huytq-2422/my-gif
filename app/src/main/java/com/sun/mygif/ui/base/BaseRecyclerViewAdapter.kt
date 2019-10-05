package com.sun.mygif.ui.base

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.sun.mygif.utils.DiffUtilCallback

abstract class BaseRecyclerViewAdapter<T, V : BaseViewHolder<T>> : RecyclerView.Adapter<V>() {

    private val items = ArrayList<T>()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: V, position: Int) {
        getItem(position)?.let { viewHolder.onBindData(it) }
    }

    protected fun getItem(position: Int): T? = if (position in 0 until itemCount) items[position] else null

    fun updateData(newItems: List<T>) = DiffUtil.calculateDiff(DiffUtilCallback(items, newItems)).also {
        items.apply {
            if(isNotEmpty()) clear()
            addAll(newItems)
        }
    }.dispatchUpdatesTo(this)

    fun insertData(insertItems: List<T>) = with(items) {
        val firstPosition = size
        addAll(insertItems)
        val secondPosition = size
        notifyItemRangeInserted(firstPosition, secondPosition - 1)
    }

    fun removeData(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
}
