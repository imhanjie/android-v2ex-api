package com.imhanjie.v2ex.api.model

/**
 * v2ex json 接口返回数据结构
 */
data class V2exResult(
    var success: Boolean,
    var message: String,
    var once: String
)