package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.V2exResult
import com.imhanjie.v2ex.api.support.V2ex
import com.imhanjie.v2ex.api.support.parseJson

object V2exResultParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2ex.BASE_URL}/thank/reply/")
    }

    override fun parser(html: String): Any {
        return parseJson<V2exResult>(html)
    }

}