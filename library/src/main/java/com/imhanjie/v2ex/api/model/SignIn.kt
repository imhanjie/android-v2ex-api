package com.imhanjie.v2ex.api.model

data class SignIn(
    val keyUserName: String,
    val keyPassword: String,
    val keyVerCode: String,
    val verUrlOnce: String
)
