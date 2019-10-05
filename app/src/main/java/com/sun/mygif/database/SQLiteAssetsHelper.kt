package com.sun.mygif.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sun.mygif.utils.BUFFER_SIZE
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val ASSETS_NAME = "databases"

open class SQLiteAssetsHelper(
    private val context: Context, private val dbName: String, dbVersion: Int
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    private val assetsPath = StringBuilder(context.applicationInfo.dataDir).apply {
        append(File.separator)
        append(ASSETS_NAME)
    }.toString()

    private val dbPath = StringBuilder(assetsPath).apply {
        append(File.separator)
        append(dbName)
    }.toString()

    private var database: SQLiteDatabase? = null

    init {
        try {
            if (!getDatabaseFile().exists()) {
                close()
                copyDatabaseFromAssets()
            }
        } catch (exception: IOException) {
            close()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) try {
            upgradeDatabase()
        } catch (exception: IOException) {
            close()
        }
    }

    @Synchronized
    final override fun close() {
        database?.close()
        super.close()
    }

    @Throws(IOException::class)
    private fun copyDatabaseFromAssets() {
        val inputStream = context.assets.open(dbName)
        val outputStream = FileOutputStream(getDatabaseFile())
        val buffer = ByteArray(BUFFER_SIZE)
        var inputLine: Int
        do {
            inputLine = inputStream.read(buffer)
            if (inputLine > 0) outputStream.write(buffer, 0, inputLine)
        } while (inputLine > 0)

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    @Throws(IOException::class)
    private fun upgradeDatabase() {
        val databaseFile = getDatabaseFile()
        if (databaseFile.exists()) databaseFile.delete()
        copyDatabaseFromAssets()
    }

    @Throws(IOException::class)
    private fun getDatabaseFile(): File {
        val folder = File(assetsPath)
        if (!folder.exists()) folder.mkdirs()
        return File(dbPath)
    }
}
