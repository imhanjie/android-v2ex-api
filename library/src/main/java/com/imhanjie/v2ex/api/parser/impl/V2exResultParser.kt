package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.model.V2exResult
import com.imhanjie.v2ex.api.parser.Parser
import com.imhanjie.v2ex.api.support.parseJson

class V2exResultParser : Parser {

    override fun parser(html: String): Any {
        return parseJson<V2exResult>(html)
    }

}