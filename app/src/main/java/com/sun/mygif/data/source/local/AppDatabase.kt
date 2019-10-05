package com.sun.mygif.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import com.sun.mygif.database.SQLiteAssetsHelper

private const val DATABASE_NAME = "gifs.db"
private const val DATABASE_VERSION = 2

class AppDatabase private constructor(context: Context) : SQLiteAssetsHelper(context, DATABASE_NAME, DATABASE_VERSION) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            sInstance ?: AppDatabase(context).also { sInstance = it }
    }
}
