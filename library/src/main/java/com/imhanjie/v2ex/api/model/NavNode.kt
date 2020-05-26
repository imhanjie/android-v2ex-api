package com.imhanjie.v2ex.api.model

data class NavNode(
    val type: String,
    val children: List<TinyNode>
)