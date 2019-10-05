package com.sun.mygif.utils

import com.sun.mygif.cache.GetInputStreamAsyncTask
import com.sun.mygif.data.source.OnDataLoadedCallback
import java.io.*

/** Get InputStream of a file loaded from url */
@Throws(FileNotFoundException::class)
fun File.getInputStream(url: String, callback: OnDataLoadedCallback<InputStream>) {
    GetInputStreamAsyncTask(this, callback).execute(url)
}

/** Returns a freshly opened InputStream for the cached file. */
@Throws(FileNotFoundException::class)
fun File.openCachedInputStream(): InputStream {
    return BufferedInputStream(FileInputStream(this))
}
