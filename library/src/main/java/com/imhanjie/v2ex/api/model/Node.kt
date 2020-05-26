package com.imhanjie.v2ex.api.model

data class Node(
    val id: Long,
    val title: String,
    val name: String,
    val avatar: String,
    val desc: String,
    val topicTotalCount: Long,
    var isFavorite: Boolean,
    val topics: List<TopicItem>,
    val currentPage: Int,
    val totalPage: Int,
    val once: String
)