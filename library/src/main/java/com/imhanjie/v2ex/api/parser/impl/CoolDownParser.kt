package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object CoolDownParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/signin/cooldown"
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        return document.selectFirst("div.topic_content.markdown_body").selectFirst("p").text()
    }

}