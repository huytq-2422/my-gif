package com.sun.mygif.data.source.local.base

import android.os.AsyncTask
import com.sun.mygif.data.source.OnDataLoadedCallback

private const val MESSAGE_NULL_RESULT = "Result is null"

class LocalAsyncTask<P, T>(
    private val handler: LocalDataHandler<P, T>,
    private val callback: OnDataLoadedCallback<T>
) : AsyncTask<P, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: P): T? {
        return try {
            handler.execute(params[0]) ?: throw Exception()
        } catch (e: Exception) {
            exception = e
            null
        }
    }

    override fun onPostExecute(result: T?) {
        result?.let {
            callback.onSuccess(result)
        } ?: callback.onFailed(exception ?: Exception(MESSAGE_NULL_RESULT))
    }
}
