package com.imhanjie.v2ex.api.model

data class Member(
    var id: Long,
    var name: String,
    var avatar: String,
    var isFollowing: Boolean,
    var isBlock: Boolean,
    var createTime: String,
    var isMe: Boolean,
    var once: String,
    var blockParamT: String
)
