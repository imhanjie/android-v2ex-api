package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyNode(
    var title: String,
    var name: String,
    var avatar: String,
    var topicTotalCount: Long
) : Parcelable