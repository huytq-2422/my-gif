package com.sun.mygif.data.source.local.dao

import android.annotation.SuppressLint
import android.content.Context
import com.sun.mygif.data.model.ParentTopic
import com.sun.mygif.data.model.Topic
import com.sun.mygif.data.source.local.AppDatabase

private const val TABLE_PARENT_TOPICS = "tbl_parent_topics"
private const val TABLE_SUB_TOPICS = "tbl_child_topics"
private const val FIELD_PARENT = "parent"

class TopicDAOImpl private constructor(context: Context) : TopicDAO {

    private val database = AppDatabase.getInstance(context).readableDatabase

    @SuppressLint("Recycle")
    override fun getParentTopics(): List<ParentTopic> {
        val cursor = database.query(TABLE_PARENT_TOPICS, null, null, null, null, null, null)
            .apply { moveToFirst() }

        return ArrayList<ParentTopic>().apply {
            while (!cursor.isAfterLast) {
                add(ParentTopic(cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
    }

    @SuppressLint("Recycle")
    override fun getSubTopics(): List<Topic> {
        val cursor = database.query(TABLE_SUB_TOPICS, null, null, null, null, null, FIELD_PARENT)
            .apply { moveToFirst() }
        return ArrayList<Topic>().apply {
            while (!cursor.isAfterLast) {
                add(Topic(cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sInstance: TopicDAOImpl? = null

        @JvmStatic
        fun getInstance(context: Context): TopicDAOImpl =
            sInstance ?: TopicDAOImpl(context).also { sInstance = it }
    }
}
