package com.sun.mygif.cache

import android.os.AsyncTask
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.utils.download
import com.sun.mygif.utils.openCachedInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class GetInputStreamAsyncTask(
    private val file: File,
    private val callback: OnDataLoadedCallback<InputStream>
) : AsyncTask<String, Void?, Exception?>() {

    override fun doInBackground(vararg params: String): Exception? {
        return try {
            if (!file.exists()) file.download(params[0])
            null
        } catch (exception: FileNotFoundException) {
            exception
        } catch (exception: IOException) {
            exception
        }
    }

    override fun onPostExecute(exception: Exception?) {
        exception?.let {
            callback.onFailed(it)
        } ?: callback.onSuccess(file.openCachedInputStream())
    }
}
