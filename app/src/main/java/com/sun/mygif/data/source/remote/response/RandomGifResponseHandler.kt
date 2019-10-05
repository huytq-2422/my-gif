package com.sun.mygif.data.source.remote.response

import com.sun.mygif.data.model.RandomGifResponse
import org.json.JSONObject

class RandomGifResponseHandler : DataResponseHandler<RandomGifResponse> {
    override fun parseToObject(jsonData: String): RandomGifResponse = RandomGifResponse(JSONObject(jsonData))
}
