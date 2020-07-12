package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicItem(
    var id: Long,
    var title: String,
    var nodeName: String,
    var nodeTitle: String,
    var userName: String,
    var userAvatar: String,
    var latestReplyTime: String,
    var replies: Long,
    var isTop: Boolean
) : Parcelable