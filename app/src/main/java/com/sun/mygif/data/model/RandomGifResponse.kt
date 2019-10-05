package com.sun.mygif.data.model

import org.json.JSONObject

private const val RESULTS = "results"
private const val MEDIA = "media"
private const val NANOGIF = "nanogif"
private const val DIMS = "dims"
private const val URL = "url"

data class RandomGifResponse(val results: List<Result>) {
    constructor(body: JSONObject) : this(results = ArrayList<Result>().apply {
        val resultsJson = body.getJSONArray(RESULTS)
        for (index in 0 until resultsJson.length()) {
            add(Result(resultsJson.getJSONObject(index)))
        }
    })

    data class Result(val medias: List<Media>) {
        constructor(resultJson: JSONObject) : this(medias = ArrayList<Media>().apply {
            val mediasJson = resultJson.getJSONArray(MEDIA)
            for (index in 0 until mediasJson.length()) {
                add(Media(mediasJson.getJSONObject(index)))
            }
        })

        data class Media(val nanoGif: NanoGif) {
            constructor(mediaJson: JSONObject) : this(
                nanoGif = NanoGif(mediaJson.getJSONObject(NANOGIF))
            )

            data class NanoGif(val width: Int, val height: Int, val url: String) {
                constructor(gifJson: JSONObject) : this(
                    width = gifJson.getJSONArray(DIMS).getInt(0),
                    height = gifJson.getJSONArray(DIMS).getInt(1),
                    url = gifJson.getString(URL)
                )
            }
        }
    }
}
