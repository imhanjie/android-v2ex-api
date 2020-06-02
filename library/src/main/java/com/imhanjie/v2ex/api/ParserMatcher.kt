package com.imhanjie.v2ex.api

interface ParserMatcher {

    fun match(url: String, method: String): Boolean

    fun parser(html: String): Any

}