package com.imhanjie.v2ex.api.model

data class NavNode(
    var type: String,
    var children: List<TinyNode>
)