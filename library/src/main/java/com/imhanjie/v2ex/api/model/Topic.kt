package com.imhanjie.v2ex.api.model

data class Topic(
    val id: Long,
    val title: String,
    val nodeName: String,
    val nodeTitle: String,
    val userName: String,
    val userAvatar: String,
    val createTime: String,
    val click: Long,
    val richContent: String,
    val subtleList: List<Subtle>,
    val replies: List<Reply>,
    val currentPage: Int,
    val totalPage: Int,
    val once: String,
    val isMyTopic: Boolean
) {

    data class Subtle(
        val no: Int,
        val createTime: String,
        val richContent: String
    )

}