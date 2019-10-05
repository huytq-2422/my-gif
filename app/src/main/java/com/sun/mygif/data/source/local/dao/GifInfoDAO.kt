package com.sun.mygif.data.source.local.dao

interface GifInfoDAO {
    fun getAll(): List<String>
    fun add(gifId: String): Boolean
    fun delete(gifId: String): Boolean
    fun findFavorite(gifId: String): Boolean
}
