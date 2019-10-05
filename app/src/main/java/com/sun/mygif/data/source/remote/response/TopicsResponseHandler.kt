package com.sun.mygif.data.source.remote.response

import com.sun.mygif.data.model.TopicsResponse
import org.json.JSONObject

class TopicsResponseHandler : DataResponseHandler<TopicsResponse> {
    override fun parseToObject(jsonData: String): TopicsResponse = TopicsResponse(JSONObject(jsonData))
}
