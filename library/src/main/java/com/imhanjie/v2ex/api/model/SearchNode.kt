package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchNode(
    val id: String,
    val text: String,
    val topics: Int
) : Parcelable