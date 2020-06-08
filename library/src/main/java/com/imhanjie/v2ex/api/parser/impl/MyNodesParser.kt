package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.MyNode
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup


object MyNodesParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/my/nodes"
    }

    override fun parser(html: String): List<MyNode> {
        val document = Jsoup.parse(html)
        return document.selectFirst("#my-nodes").select("a").map {
            val title = it.selectFirst("div").ownText()
            val name = it.attr("href").split("/").last()
            val avatarSrc = it.selectFirst("img").attr("src")
            val avatar = if (avatarSrc.startsWith("https://")) avatarSrc else ""
            val topicTotalCount = it.selectFirst("span").ownText().toLong()
            MyNode(
                title,
                name,
                avatar,
                topicTotalCount
            )
        }
    }

}