package com.sun.mygif.utils

import java.io.*
import java.net.URL

/** Download the image into the cache. */
@Throws(IOException::class)
fun File.download(url: String) {
    var inputStream: InputStream? = null
    var outputStream: BufferedOutputStream? = null

    try {
        inputStream = URL(url).content as InputStream
        outputStream = BufferedOutputStream(FileOutputStream(this))

        val bytes = ByteArray(BUFFER_SIZE)
        var hasBytes: Boolean
        do {
            inputStream.read(bytes).also {
                hasBytes = it != -1
                outputStream.apply {
                    if (hasBytes) write(bytes, 0, it)
                }
            }
        } while (hasBytes)

        outputStream.flush()
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}
