package com.imhanjie.v2ex.api.model

data class Notifications(
    var items: List<Item>,
    var currentPage: Int,
    var totalPage: Int
) {

    data class Item(
        var userAvatar: String,
        var titleRichContent: String,
        var replyRichContent: String,
        var createTime: String
    )

}