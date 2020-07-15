package com.imhanjie.v2ex.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Topic(
    var id: Long,
    var title: String,
    var nodeName: String,
    var nodeTitle: String,
    var userName: String,
    var userAvatar: String,
    var createTime: String,
    var click: Long,
    var richContent: String,
    var subtleList: List<Subtle>,
    var replies: List<Reply>,
    var currentPage: Int,
    var totalPage: Int,
    var once: String,
    var isMyTopic: Boolean,
    var favoriteParam: String,
    var isFavorite: Boolean,
    var canAppend: Boolean
) : Parcelable {

    @Parcelize
    data class Subtle(
        var no: Int,
        var createTime: String,
        var richContent: String
    ) : Parcelable

}