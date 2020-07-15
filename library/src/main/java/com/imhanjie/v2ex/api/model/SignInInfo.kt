package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignInInfo(
    var keyUserName: String,
    var keyPassword: String,
    var keyVerCode: String,
    var verUrlOnce: String
) : Parcelable
