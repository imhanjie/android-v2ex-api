package com.imhanjie.v2ex.api.model

data class MemberTopics(
    var topics: List<TopicItem>,
    var hide: Boolean,
    var totalCount: Int,
    var currentPage: Int,
    var totalPage: Int
)