package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyUserInfo(
    var name: String,
    var avatar: String,
    var nodeStars: Long,
    var topicStars: Long,
    var following: Long,
    var moneyGold: Long,
    var moneySilver: Long,
    var moneyBronze: Long
) : Parcelable