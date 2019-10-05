package com.sun.mygif.utils

import android.content.res.Resources
import com.sun.mygif.R

const val EXTRA_ACTION_BAR_INFO = "com.sun.mygif.utils.EXTRA_ACTION_BAR_INFO"
const val EXTRA_GIF_ID = "com.sun.mygif.utils.EXTRA_GIF_ID"
const val EXTRA_SEARCH_KEY = "com.sun.mygif.utils.EXTRA_SEARCH_KEY"
const val EXTRA_GIF_INFO = "com.sun.mygif.utils.EXTRA_GIF_INFO"
const val EXTRA_GIF_DETAIL = "com.sun.mygif.utils.EXTRA_GIF_DETAIL"

const val ACTION_FINISH_ACTIVITY = "com.sun.mygif.ui.main.ACTION_FINISH_ACTIVITY"

const val CATEGORY_TRENDING = "com.sun.mygif.utils.CATEGORY_TRENDING"
const val CATEGORY_DEFAULT = "Sun Asterisk"
const val EMPTY_ID = "com.sun.mygif.utils.EMPTY_ID"
const val EMPTY_PARAMS = "com.sun.mygif.utils.EMPTY_PARAMS"
const val EMPTY_TAG = "com.sun.mygif.utils.EMPTY_TAG"
const val METHOD_GET = "GET"

const val GIF_ID_DEFAULT = "3oEduUGL2JaSK7oS76"

const val SEPARATOR_PROPERTY = "_"
const val BUFFER_SIZE = 65536

val screenWidth = Resources.getSystem().displayMetrics.widthPixels
val screenHeight = Resources.getSystem().displayMetrics.heightPixels
val basicColors = listOf(
    R.color.color_yellow,
    R.color.color_fuchsia,
    R.color.color_red,
    R.color.color_silver,
    R.color.color_gray,
    R.color.color_gray,
    R.color.color_olive,
    R.color.color_purple,
    R.color.color_maroon,
    R.color.color_aqua,
    R.color.color_lime,
    R.color.color_teal,
    R.color.color_green,
    R.color.color_blue,
    R.color.color_navy
)
val gradientColors = listOf(
    R.drawable.bg_gradient_aqua,
    R.drawable.bg_gradient_fuchsia,
    R.drawable.bg_gradient_gray,
    R.drawable.bg_gradient_lime,
    R.drawable.bg_gradient_maroon,
    R.drawable.bg_gradient_olive,
    R.drawable.bg_gradient_purple,
    R.drawable.bg_gradient_red,
    R.drawable.bg_gradient_silver,
    R.drawable.bg_gradient_teal
)
