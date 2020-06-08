package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.TopicItem
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object LatestTopicsParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2ex.BASE_URL}/recent?p=")
                || url.startsWith("${V2ex.BASE_URL}/?tab=")
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        return document.select("div.cell.item").map { eCell ->
            val eTitle = eCell.selectFirst("a.topic-link")
            val isTop = eCell.attr("style").isNotEmpty()
            val title = eTitle.text()
            val id = eTitle.attr("href").split("/")[2].split("#")[0].toLong()

            val eTopicInfo = eCell.selectFirst("span.topic_info")

            val eNode = eTopicInfo.selectFirst("a.node")
            val nodeTitle = eNode.text()
            val nodeName = eNode.attr("href").split("/")[2]

            val userName = eTopicInfo.selectFirst("strong > a").text()

            val userAvatar = eCell.selectFirst("img.avatar").attr("src")
            val latestReplyTime = eTopicInfo.text().split(" • ")[2]

            val replies = eCell.selectFirst("a.count_livid")?.text()?.toLong() ?: 0

            TopicItem(
                id,
                title,
                nodeName,
                nodeTitle,
                userName,
                userAvatar,
                latestReplyTime,
                replies,
                isTop
            )
        }
    }

}