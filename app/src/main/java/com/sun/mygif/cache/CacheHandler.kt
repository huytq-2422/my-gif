package com.sun.mygif.cache

import android.content.Context
import android.content.res.Resources
import com.sun.mygif.R
import com.sun.mygif.utils.computeMD5Hash
import com.sun.mygif.utils.toHexString
import java.io.File
import java.io.IOException

private const val NAME_SUB_DIR = "GifView"

class CacheHandler(private val context: Context) {
    fun getFile(url: String): File? {
        return try {
            getCacheSubDir(context).let {
                if (!it.exists() && !it.mkdirs()) {
                    throw IOException(
                        String.format(
                            Resources.getSystem().getString(R.string.format_exception_io),
                            it.absolutePath
                        )
                    )
                }
                val pathName = StringBuilder(it.absolutePath).apply {
                    append(File.separator)
                    append(url.computeMD5Hash().toHexString())
                }.toString()

                File(pathName)
            }
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    companion object {
        private var cacheSubDir: File? = null
        @JvmStatic
        @Throws(IOException::class)
        private fun getCacheSubDir(context: Context): File {
            val cacheSubDirectoryPathName = StringBuilder(context.cacheDir.toString()).apply {
                append(File.separator)
                append(NAME_SUB_DIR)
            }.toString()
            return cacheSubDir ?: File(cacheSubDirectoryPathName)
        }
    }
}
