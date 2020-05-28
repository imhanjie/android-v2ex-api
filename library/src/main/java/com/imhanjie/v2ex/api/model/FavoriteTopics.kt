package com.imhanjie.v2ex.api.model

data class FavoriteTopics(
    var topics: List<TopicItem>,
    var currentPage: Int,
    var totalPage: Int
)