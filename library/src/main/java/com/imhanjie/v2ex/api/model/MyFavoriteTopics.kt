package com.imhanjie.v2ex.api.model

data class MyFavoriteTopics(
    var topics: List<TopicItem>,
    var currentPage: Int,
    var totalPage: Int
)