package com.sun.mygif.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActionBarInfo(val title: String, val iconId: Int) : Parcelable
