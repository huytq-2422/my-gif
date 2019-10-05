package com.sun.mygif.data.model

import android.os.Parcelable
import com.sun.mygif.utils.EMPTY_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GifSmall(val id: String, val width: Int, val height: Int, val url: String) : Parcelable {
    constructor(width: Int, height: Int, url: String) : this(EMPTY_ID, width, height, url)

    constructor(responseData: GifResponse.Data) : this(
        id = responseData.id,
        width = responseData.images.fixedHeightSmall.width.toInt(),
        height = responseData.images.fixedHeightSmall.height.toInt(),
        url = responseData.images.fixedHeightSmall.url
    )
}
