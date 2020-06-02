package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.V2exResult
import com.imhanjie.v2ex.api.support.V2exConstants
import com.imhanjie.v2ex.api.support.parseJson

class V2exResultParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2exConstants.BASE_URL}/thank/reply/")
    }

    override fun parser(html: String): Any {
        return parseJson<V2exResult>(html)
    }

}