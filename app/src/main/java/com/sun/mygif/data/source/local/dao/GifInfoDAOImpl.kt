package com.sun.mygif.data.source.local.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.sun.mygif.data.source.local.AppDatabase

private const val TABLE_FAVORITE_GIFS = "tbl_favorite_gifs"
private const val ORDER_REVRESE = "id DESC"
private const val FIELD_GIFID = "gifId"
private const val SELECTION_LIKE_GIFID = "gifId LIKE ?"

class GifInfoDAOImpl private constructor(context: Context) : GifInfoDAO {

    private val database = AppDatabase.getInstance(context).writableDatabase

    @SuppressLint("Recycle")
    override fun getAll(): List<String> {
        val cursor = database.query(TABLE_FAVORITE_GIFS, null, null, null, null, null, ORDER_REVRESE)
            .apply { moveToFirst() }
        return ArrayList<String>().apply {
            while (!cursor.isAfterLast) {
                add(cursor.getString(cursor.getColumnIndex(FIELD_GIFID)))
                cursor.moveToNext()
            }
            cursor.close()
        }
    }

    override fun add(gifId: String): Boolean {
        if (findFavorite(gifId)) return false
        val contentValues = ContentValues().apply {
            put(FIELD_GIFID, gifId)
        }
        return database.insert(TABLE_FAVORITE_GIFS, null, contentValues) > 0
    }

    override fun delete(gifId: String): Boolean {
        val selectionArgs = arrayOf(gifId)
        return database.delete(TABLE_FAVORITE_GIFS, SELECTION_LIKE_GIFID, selectionArgs) > 0
    }

    override fun findFavorite(gifId: String): Boolean {
        val selectionArgs = arrayOf(gifId)
        val cursor = database.query(TABLE_FAVORITE_GIFS, null, SELECTION_LIKE_GIFID, selectionArgs, null, null, null)
        return (cursor.count > 0).also { cursor.close() }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sInstance: GifInfoDAOImpl? = null

        @JvmStatic
        fun getInstance(context: Context): GifInfoDAOImpl =
            sInstance ?: GifInfoDAOImpl(context).also { sInstance = it }
    }
}
