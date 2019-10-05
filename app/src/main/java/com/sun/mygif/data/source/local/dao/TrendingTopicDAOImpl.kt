package com.sun.mygif.data.source.local.dao

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.sun.mygif.data.model.Topic
import com.sun.mygif.utils.decodeToTopic

class TrendingTopicDAOImpl private constructor(private val sharedPreferences: SharedPreferences) : TrendingTopicDAO {
    override fun getSavedTopicsByTitles(titles: List<String>): List<Topic?> = titles.map {
        sharedPreferences.getString(it, null)?.decodeToTopic()
    }

    override fun saveTopic(topic: Topic) = with(sharedPreferences.edit()) {
        putString(topic.title, topic.encryptToString())
        apply()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sInstance: TrendingTopicDAOImpl? = null

        @JvmStatic
        fun getInstance(sharedPreferences: SharedPreferences): TrendingTopicDAOImpl =
            sInstance ?: TrendingTopicDAOImpl(sharedPreferences).also { sInstance = it }
    }
}
