package com.sun.mygif.data.model

import org.json.JSONObject

private const val RESULTS = "results"

data class TopicsResponse(val results: List<String>) {
    constructor(data: JSONObject) : this(results = ArrayList<String>().apply {
        val results = data.getJSONArray(RESULTS)
        for (index in 0 until results.length()) {
            add(results.getString(index))
        }
    })
}
