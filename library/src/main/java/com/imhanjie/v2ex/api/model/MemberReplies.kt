package com.imhanjie.v2ex.api.model

data class MemberReplies(
    var replies: List<Item>,
    var totalCount: Int,
    var currentPage: Int,
    var totalPage: Int
) {

    data class Item(
        var titleRichContent: String,
        var createTime: String,
        var replyRichContent: String
    )

}