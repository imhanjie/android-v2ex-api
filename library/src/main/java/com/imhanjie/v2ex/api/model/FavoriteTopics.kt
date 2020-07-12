package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteTopics(
    var topics: List<TopicItem>,
    var currentPage: Int,
    var totalPage: Int
) : Parcelable