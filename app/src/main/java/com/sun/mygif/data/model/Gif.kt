package com.sun.mygif.data.model

import android.os.Parcelable
import com.sun.mygif.utils.EMPTY_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gif(val id: String, val width: Int, val height: Int, val url: String) : Parcelable {
    constructor(width: Int, height: Int, url: String) : this(EMPTY_ID, width, height, url)

    constructor(responseData: GifResponse.Data) : this(
        id = responseData.id,
        width = responseData.images.downsizedMedium.width.toInt(),
        height = responseData.images.downsizedMedium.height.toInt(),
        url = responseData.images.downsizedMedium.url
    )

    constructor(responseData: GifsResponse.Data) : this(
        id = responseData.id,
        width = responseData.images.fixedWidthDownsampled.width.toInt(),
        height = responseData.images.fixedWidthDownsampled.height.toInt(),
        url = responseData.images.fixedWidthDownsampled.url
    )

    constructor(responseResult: RandomGifResponse.Result) : this(
        id = EMPTY_ID,
        width = responseResult.medias[0].nanoGif.width,
        height = responseResult.medias[0].nanoGif.height,
        url = responseResult.medias[0].nanoGif.url
    )
}
