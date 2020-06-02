package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import org.jsoup.Jsoup

class CoolDownParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url == "/signin/cooldown"
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        return document.selectFirst("div.topic_content.markdown_body").selectFirst("p").text()
    }

}