package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.parser.Parser
import org.jsoup.Jsoup

object SignInProblemParser : Parser {

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        return document.selectFirst("div.problem").selectFirst("li").text()
    }

}