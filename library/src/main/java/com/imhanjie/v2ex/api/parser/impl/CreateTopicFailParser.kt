package com.imhanjie.v2ex.api.parser.impl

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object CreateTopicFailParser : ParserMatcher {

    @SuppressLint("DefaultLocale")
    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/new" && method.toUpperCase() == "POST"
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val array = document.selectFirst("div.problem").selectFirst("ul").select("li")
        return array.joinToString("\n") { it.text() }
    }

}