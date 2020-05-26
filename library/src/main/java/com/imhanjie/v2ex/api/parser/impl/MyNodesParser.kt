package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.model.MyNode
import com.imhanjie.v2ex.api.parser.Parser
import org.jsoup.Jsoup


class MyNodesParser : Parser {

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