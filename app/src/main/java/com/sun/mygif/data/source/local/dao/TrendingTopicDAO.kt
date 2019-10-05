package com.sun.mygif.data.source.local.dao

import com.sun.mygif.data.model.Topic

interface TrendingTopicDAO{
    fun getSavedTopicsByTitles(titles: List<String>): List<Topic?>
    fun saveTopic(topic: Topic)
}
