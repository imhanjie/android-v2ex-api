package com.imhanjie.v2ex.api.model

data class Reply(
    var id: Long,
    var userAvatar: String,
    var userName: String,
    var content: String,
    var time: String,
    var thankCount: Long,
    var thanked: Boolean,
    var no: Long
)