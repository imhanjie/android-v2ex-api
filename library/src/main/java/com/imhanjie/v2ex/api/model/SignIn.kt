package com.imhanjie.v2ex.api.model

data class SignIn(
    var keyUserName: String,
    var keyPassword: String,
    var keyVerCode: String,
    var verUrlOnce: String
)
