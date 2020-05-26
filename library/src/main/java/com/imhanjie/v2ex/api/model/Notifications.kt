package com.imhanjie.v2ex.api.model

data class Notifications(
    val items: List<Item>,
    val currentPage: Int,
    val totalPage: Int
) {

    data class Item(
        val userAvatar: String,
        val titleRichContent: String,
        val replyRichContent: String,
        val createTime: String
    )

}