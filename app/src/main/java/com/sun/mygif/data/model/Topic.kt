package com.sun.mygif.data.model

import android.database.Cursor
import com.sun.mygif.utils.SEPARATOR_PROPERTY

private const val FIELD_NAME = "name"
private const val FIELD_WIDTH = "width"
private const val FIELD_HEIGHT = "height"
private const val FIELD_URL = "url"

data class Topic(val title: String, val gifBackground: Gif) {
    constructor(title: String, width: Int, height: Int, url: String) : this(title, Gif(width, height, url))

    constructor(cursor: Cursor) : this(
        title = cursor.getString(cursor.getColumnIndex(FIELD_NAME)),
        width = cursor.getInt(cursor.getColumnIndex(FIELD_WIDTH)),
        height = cursor.getInt(cursor.getColumnIndex(FIELD_HEIGHT)),
        url = cursor.getString(cursor.getColumnIndex(FIELD_URL))
    )

    fun encryptToString(): String = StringBuilder().apply {
        append(title)
        append(SEPARATOR_PROPERTY)
        append(gifBackground.id)
        append(SEPARATOR_PROPERTY)
        append(gifBackground.width)
        append(SEPARATOR_PROPERTY)
        append(gifBackground.height)
        append(SEPARATOR_PROPERTY)
        append(gifBackground.url)
    }.toString()
}
