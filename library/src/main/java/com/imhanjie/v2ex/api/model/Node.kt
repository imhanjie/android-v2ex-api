package com.imhanjie.v2ex.api.model

data class Node(
    var id: Long,
    var title: String,
    var name: String,
    var avatar: String,
    var desc: String,
    var topicTotalCount: Long,
    var isFavorite: Boolean,
    var topics: List<TopicItem>,
    var currentPage: Int,
    var totalPage: Int,
    var once: String
)