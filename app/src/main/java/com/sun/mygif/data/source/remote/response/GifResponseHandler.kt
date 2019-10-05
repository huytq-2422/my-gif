package com.sun.mygif.data.source.remote.response

import com.sun.mygif.data.model.GifResponse
import org.json.JSONObject

class GifResponseHandler : DataResponseHandler<GifResponse> {
    override fun parseToObject(jsonData: String): GifResponse = GifResponse(JSONObject(jsonData))
}
