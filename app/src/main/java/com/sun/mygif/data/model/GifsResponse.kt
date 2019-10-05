package com.sun.mygif.data.model

import org.json.JSONObject

private const val ID = "id"
private const val IMAGES = "images"
private const val FIXED_WIDTH_DOWNSAMPLED = "fixed_width_downsampled"
private const val WIDTH = "width"
private const val HEIGHT = "height"
private const val URL = "url"
private const val DATA = "data"
private const val TITLE = "title"

data class GifsResponse(val data: List<Data>) {
    constructor(jsonObject: JSONObject) : this(
        data = ArrayList<Data>().apply {
            val dataList = jsonObject.getJSONArray(DATA)
            for (index in 0 until dataList.length())
                add(Data(dataList.getJSONObject(index)))
        }
    )

    data class Data(val id: String, val title: String, val images: Image) {
        constructor(jsonObject: JSONObject) : this(
            id = jsonObject.getString(ID),
            title = jsonObject.getString(TITLE),
            images = Image(jsonObject.getJSONObject(IMAGES))
        )

        data class Image(val fixedWidthDownsampled: FixedWidthDownsampled) {
            constructor(jsonObject: JSONObject) : this(
                fixedWidthDownsampled = FixedWidthDownsampled(jsonObject.getJSONObject(FIXED_WIDTH_DOWNSAMPLED))
            )

            data class FixedWidthDownsampled(val width: String, val height: String, val url: String) {
                constructor(jsonObject: JSONObject) : this(
                    width = jsonObject.getString(WIDTH),
                    height = jsonObject.getString(HEIGHT),
                    url = jsonObject.getString(URL)
                )
            }
        }
    }
}
