package com.imhanjie.v2ex.api.model

data class TopicItem(
    val id: Long,
    val title: String,
    val nodeName: String,
    val nodeTitle: String,
    val userName: String,
    val userAvatar: String,
    val latestReplyTime: String,
    val replies: Long,
    val isTop: Boolean
)