package com.sun.mygif.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Movie
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.sun.mygif.cache.CacheHandler
import com.sun.mygif.data.source.OnDataLoadedCallback
import com.sun.mygif.utils.getInputStream
import com.sun.mygif.utils.gradientColors
import java.io.FileNotFoundException
import java.io.InputStream

const val BASE_WIDTH = 1
const val BASE_HEIGHT = 2
private const val TAG = "GifView"
private const val MOVIE_DURATION_DEFAULT = 1000

class GifView : View {

    var gifUrl: String? = null
        set(url) {
            field = url
            movie = null
            setPlaceholder()
            url?.let { loadGif(it) }
        }
    private var startTime: Long = 0
    private var movie: Movie? = null
    private val cacheHandler = CacheHandler(context)
    private val placeholderColorId = gradientColors.random()

    init {
        isFocusable = true
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        val currentTime = android.os.SystemClock.uptimeMillis()
        if (startTime == 0L) startTime = currentTime

        movie?.draw(canvas, currentTime) ?: setPlaceholder()
        invalidate()
    }

    // scale view baseUrl on width or height and original ratio
    fun setRatioSize(ratio: Float, size: Int, base: Int) {
        layoutParams.apply {
            width = if (base == BASE_WIDTH) size else (size * ratio).toInt()
            height = if (base == BASE_HEIGHT) size else (size / ratio).toInt()
        }
    }

    // load data from gifUrl to gifview
    private fun loadGif(urlString: String) {
        try {
            cacheHandler.getFile(urlString)?.getInputStream(urlString, object : OnDataLoadedCallback<InputStream> {
                override fun onSuccess(data: InputStream) {
                    movie = Movie.decodeStream(data)
                    invalidate()
                    requestLayout()
                }

                override fun onFailed(exception: Exception) {
                    Log.e(TAG, exception.message)
                }
            })
        } catch (exception: FileNotFoundException) {
            Log.e(TAG, exception.message)
        }
    }

    private fun Movie.draw(canvas: Canvas?, currentTime: Long) {
        val movieDuration = if (duration() == 0) MOVIE_DURATION_DEFAULT else duration()
        val updatedTime = ((currentTime - startTime) % movieDuration).toInt()

        setTime(updatedTime)
        canvas?.scale(getScaleWidth(), getScaleHeight())
        draw(canvas, 0f, 0f)
    }

    private fun setPlaceholder() = setBackgroundResource(placeholderColorId)

    private fun Movie.getScaleWidth() = measuredWidth.toFloat() / width()

    private fun Movie.getScaleHeight() = measuredHeight.toFloat() / height()
}
/*
┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌
┌┘┌┘┌█████┌┘█████┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘
┘┌┘┌██████████████┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌
┌┘┌████┘┌████┘┌┘███┘┌┘┌┘┌┘┌┘┌┘███████┘┌┘
┘┌┘███┘┌┘┌██┘┌┘┌┘██┌┘┌┘┌┘┌┘┌┘████████┌┘┌
┌┘┌██┘┌┘┌┘┌┘┌┘┌┘┌███┌┘┌┘┌┘┌┘┌┘┌┘█████┘┌┘
┘┌┘██┌┘┌┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘┌┘┌┘███┘██┌┘┌
┌┘┌██┘┌┘┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌┘┌┘┌┘███┘┌██┘┌┘
┘┌┘██┌┘┌┘┌┘┌┘┌┘┌┘███┘┌┘┌┘┌┘┌┘███┘┌┘┘┌┌┘┌
┌┘┌███┌┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘┌┘███┘┌┘┌┘┌┘┌┘
┘┌┘┌██┘┌┘┌┘┌┘┌┘┌███┌███┌┘┌┘███┘┌┘┌┘┌┘┌┘┌
┌┘┌┘███┘┌┘┌┘┌┘┌███┘█████┌┘█████┘┌┘┌┘┌┘┌┘
┘┌┘┌┘███┘┌┘┌┘┌███┌███┌█████┌┘███┘┌┘┌┘┌┘┌
┌┘┌┘┌┘███┘┌┘┌███┌██┘┌┘┌███┌┘┌┘██┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘███┘┌███┌┘██┌┘┌┘┌█┌┘┌┘┌███┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘██████┌┘██┌┘┌┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌███┌┘┌██┘┌┘┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘┌┘██┌┘┌┘██┌┘┌┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘██┌┘┌┘┌┘┌┘┌┘┌███┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌██┘┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌██┘┌┘┌┘┌┘┌┘███┘┌┘┌┘┌┘┌
┌┘┌┘┌┘██████████┌┘███┘┌┘┌┘┌┘███┘┌┘┌┘┌┘┌┘
┘┌┘┌┘┌██████████┘┌┘███┘┌┘┌┘███┘┌┘┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌┘┌┘███┘┌┘███┘┌┘┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘┌┘███┘███┘┌┘┌┘┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘┌┘██┌┘┌┘┌┘┌┘┌┘█████┘┌┘┌┘┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌██┘┌┘┌┘┌┘┌┘┌┘███┘┌┘┌┘┌┘┌┘┌┘┌┘┌
┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘
┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌┘┌
 */
