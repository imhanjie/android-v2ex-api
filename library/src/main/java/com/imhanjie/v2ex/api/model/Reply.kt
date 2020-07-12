package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reply(
    var id: Long,
    var userAvatar: String,
    var userName: String,
    var content: String,
    var time: String,
    var thankCount: Long,
    var thanked: Boolean,
    var no: Long
) : Parcelable