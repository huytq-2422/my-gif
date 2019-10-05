package com.sun.mygif.ui.explore

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.mygif.R
import com.sun.mygif.data.model.ExploreTopic
import com.sun.mygif.data.model.Topic
import com.sun.mygif.ui.base.BaseRecyclerViewAdapter
import com.sun.mygif.ui.base.BaseViewHolder
import com.sun.mygif.ui.home.TopicAdapter
import kotlinx.android.synthetic.main.item_explore.view.*

class ExploreAdapter(
    private val onItemClick: (topic: Topic) -> Unit
) : BaseRecyclerViewAdapter<ExploreTopic, ExploreAdapter.ExploreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int) = ExploreViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_explore, parent, false),
        RecyclerView.RecycledViewPool(),
        onItemClick
    )

    class ExploreViewHolder(
        itemView: View,
        viewPool: RecyclerView.RecycledViewPool,
        onItemClick: (topic: Topic) -> Unit
    ) : BaseViewHolder<ExploreTopic>(itemView) {

        private val topicAdapter = TopicAdapter {
            onItemClick(it)
        }

        init {
            itemView.recyclerSubTopics?.run {
                setRecycledViewPool(viewPool)
                adapter = topicAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        override fun onBindData(itemData: ExploreTopic) {
            super.onBindData(itemData)

            itemView.titleParentTopic?.text = itemData.parentTopic.name
            topicAdapter.updateData(itemData.subTopics)
        }
    }
}
