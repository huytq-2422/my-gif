package com.sun.mygif.data.source.remote.response

import android.os.AsyncTask
import com.sun.mygif.data.source.OnDataLoadedCallback
import org.json.JSONException
import java.io.IOException

class GetResponsesAsync<T>(
    private val responseHandler: DataResponseHandler<T>,
    private val callback: OnDataLoadedCallback<List<T?>>
) : AsyncTask<List<String>, Void, Exception?>() {

    private val results = ArrayList<T?>()

    override fun doInBackground(vararg params: List<String>): Exception? =
        try {
            results.addAll(params[0].map {
                responseHandler.getResponse(it)
            })
            null
        } catch (exception: IOException) {
            exception
        } catch (exception: JSONException) {
            exception
        }

    override fun onPostExecute(exception: Exception?) {
        exception?.let {
            callback.onFailed(it)
        } ?: callback.onSuccess(results)
    }
}
