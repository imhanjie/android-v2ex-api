package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateTopicInfo(
    var once: String
) : Parcelable
