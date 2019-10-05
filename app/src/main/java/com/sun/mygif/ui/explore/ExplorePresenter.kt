package com.sun.mygif.ui.explore

import com.sun.mygif.data.model.ExploreTopic
import com.sun.mygif.data.model.ParentTopic
import com.sun.mygif.data.model.Topic
import com.sun.mygif.data.repository.TopicRepository
import com.sun.mygif.data.source.OnDataLoadedCallback

class ExplorePresenter(
    private val exploreView: ExploreContract.View,
    private val topicRepository: TopicRepository
) : ExploreContract.Presenter {

    override fun start() {
        exploreView.showLoading()
        getExploreTopics()
    }

    private fun getExploreTopics() {
        topicRepository.getParentTopics(object : OnDataLoadedCallback<List<ParentTopic>> {
            override fun onSuccess(data: List<ParentTopic>) = getSubTopics(parentTopics = data)

            override fun onFailed(exception: Exception) = with(exploreView) {
                hideLoading()
                notifyExceptionMessage(exception)
            }
        })
    }

    private fun getSubTopics(parentTopics: List<ParentTopic>) {
        topicRepository.getSubTopics(object : OnDataLoadedCallback<List<Topic>> {
            override fun onSuccess(data: List<Topic>) {
                var fromIndex = 0
                var toIndex = 0

                val exploreTopics = parentTopics.map { parentTopic ->
                    toIndex = fromIndex + parentTopic.numberChildren
                    ExploreTopic(parentTopic = parentTopic, subTopics = data.subList(fromIndex, toIndex))
                        .also {
                            fromIndex = toIndex
                        }
                }
                exploreView.run {
                    showExploreTopics(exploreTopics)
                    hideLoading()
                }
            }

            override fun onFailed(exception: Exception) = with(exploreView) {
                hideLoading()
                notifyExceptionMessage(exception)
            }
        })
    }
}
