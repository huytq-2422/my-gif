package com.sun.mygif.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GifDetail(val title: String, val source: String, val tags: List<String>, val gif: Gif, val gifSmall: GifSmall) : Parcelable {
    constructor(responseData: GifResponse.Data) : this(
        title = responseData.title,
        source = responseData.sourceTld,
        tags = responseData.tags,
        gif = Gif(responseData),
        gifSmall = GifSmall(responseData)
    )
}
