package com.sun.mygif.data.model

import android.database.Cursor

private const val FIELD_ID = "id"
private const val FIELD_NAME = "name"
private const val FIELD_NUMBER_CHILDREN = "numberChildren"

data class ParentTopic(val id: String, val name: String, val numberChildren: Int) {
    constructor(cursor: Cursor) : this(
        id = cursor.getString(cursor.getColumnIndex(FIELD_ID)),
        name = cursor.getString(cursor.getColumnIndex(FIELD_NAME)),
        numberChildren = cursor.getInt(cursor.getColumnIndex(FIELD_NUMBER_CHILDREN))
    )
}
