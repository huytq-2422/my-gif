package com.sun.mygif.data.source.remote.response

import com.sun.mygif.data.model.GifsResponse
import org.json.JSONObject

class GifsResponseHandler : DataResponseHandler<GifsResponse> {
    override fun parseToObject(jsonData: String): GifsResponse = GifsResponse(JSONObject(jsonData))
}
