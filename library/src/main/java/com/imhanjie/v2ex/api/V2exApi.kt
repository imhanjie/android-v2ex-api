package com.imhanjie.v2ex.api

object V2exApi {

    internal var a2CookieProvider: (() -> String?)? = null

    fun init(cookieProvider: (() -> String?)) {
        this.a2CookieProvider = cookieProvider
    }

}