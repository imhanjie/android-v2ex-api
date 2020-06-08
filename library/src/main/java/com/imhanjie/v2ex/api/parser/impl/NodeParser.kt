package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.TinyNode
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object NodeParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/planes"
    }

    override fun parser(html: String): List<TinyNode> {
        val document = Jsoup.parse(html)
        val nodeList = mutableListOf<TinyNode>()
        for (eInner in document.selectFirst("#Main").select("div.inner")) {
            for (item in eInner.select("a")) {
                val name = item.attr("href").split("/")[2]
                val title = item.text()
                nodeList.add(TinyNode(title, name))
            }
        }
        return nodeList
    }

}