package com.sun.mygif.ui.home

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.mygif.data.model.Topic
import com.sun.mygif.ui.base.BaseRecyclerViewAdapter
import com.sun.mygif.ui.base.BaseViewHolder
import com.sun.mygif.widget.BASE_HEIGHT
import kotlinx.android.synthetic.main.item_trending_topic.view.*

private const val TOPIC_WIDTH_DP = 150f

class TopicAdapter(
    private val onItemClick: (topic: Topic) -> Unit
) : BaseRecyclerViewAdapter<Topic, TopicAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TopicViewHolder = TopicViewHolder(
        LayoutInflater.from(parent.context).inflate(com.sun.mygif.R.layout.item_trending_topic, parent, false),
        onItemClick
    )
    inner class TopicViewHolder(
        itemView: View,
        private val onItemClick: (topic: Topic) -> Unit
    ) : BaseViewHolder<Topic>(itemView) {

        override fun onBindData(itemData: Topic) {
            super.onBindData(itemData)

            itemView.apply {
                titleItemTopic.text = itemData.title
                gifItemTopic.apply {
                    setRatioSize(
                        ratio = itemData.gifBackground.width.toFloat() / itemData.gifBackground.height,
                        size = getPixel(context, TOPIC_WIDTH_DP).toInt(),
                        base = BASE_HEIGHT
                    )
                    gifUrl = itemData.gifBackground.url
                }
            }
        }
        override fun onHandleItemClick(mainItem: Topic) = onItemClick(mainItem)

        private fun getPixel(context: Context, dipValue: Float): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.resources.displayMetrics)
    }
}
