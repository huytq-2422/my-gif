package com.sun.mygif.data.source.local.dao

import com.sun.mygif.data.model.ParentTopic
import com.sun.mygif.data.model.Topic

interface TopicDAO {
    fun getParentTopics(): List<ParentTopic>
    fun getSubTopics(): List<Topic>
}
