package com.imhanjie.v2ex.api.parser.impl

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.CreateTopicInfo
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object AppendTopicInfoParser : ParserMatcher {

    @SuppressLint("DefaultLocale")
    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2ex.BASE_URL}/append/topic/") && method.toUpperCase() == "GET"
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val once = document.select("input").firstOrNull { it.attr("name") == "once" }?.attr("value") ?: ""
        return CreateTopicInfo(once)
    }

}