package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notifications(
    var items: List<Item>,
    var currentPage: Int,
    var totalPage: Int
) : Parcelable {

    @Parcelize
    data class Item(
        var id: String,
        var userAvatar: String,
        var titleRichContent: String,
        var replyRichContent: String,
        var createTime: String
    ) : Parcelable

}