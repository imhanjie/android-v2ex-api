package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member(
    var id: Long,
    var name: String,
    var avatar: String,
    var isFollowing: Boolean,
    var isBlock: Boolean,
    var createTime: String,
    var isMe: Boolean,
    var once: String,
    var blockParamT: String
) : Parcelable
