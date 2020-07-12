package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberTopics(
    var topics: List<TopicItem>,
    var hide: Boolean,
    var totalCount: Int,
    var currentPage: Int,
    var totalPage: Int
) : Parcelable