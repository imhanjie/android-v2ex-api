package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberReplies(
    var replies: List<Item>,
    var totalCount: Int,
    var currentPage: Int,
    var totalPage: Int
) : Parcelable {

    @Parcelize
    data class Item(
        var titleRichContent: String,
        var createTime: String,
        var replyRichContent: String
    ) : Parcelable

}