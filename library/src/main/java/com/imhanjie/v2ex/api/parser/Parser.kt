package com.imhanjie.v2ex.api.parser

interface Parser {

    fun parser(html: String): Any

}