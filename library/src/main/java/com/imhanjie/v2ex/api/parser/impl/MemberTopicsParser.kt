package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.MemberTopics
import com.imhanjie.v2ex.api.model.TopicItem
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup
import java.util.regex.Pattern
import kotlin.math.max

object MemberTopicsParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return Pattern.compile("^${V2ex.BASE_URL}/member/\\w+/topics\\?p=\\d+\$").matcher(url).find()
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val currentPage = document.selectFirst("a.page_current")?.text()?.toInt() ?: 1
        var totalPage = currentPage
        with(document.select("a.page_normal")!!) {
            if (isNotEmpty()) {
                totalPage = max(last().text().toInt(), currentPage)
            }
        }

        val hide = document.selectFirst("#Main").selectFirst("div.box").selectFirst("div.inner") != null
        val totalCount = document.selectFirst("div.header").selectFirst("strong.gray")?.text()?.toInt() ?: 0
        val topics = document.select("div.cell.item").map { eCell ->
            val eTitle = eCell.selectFirst("a.topic-link")
            val isTop = eCell.attr("style").isNotEmpty()
            val title = eTitle.text()
            val id = eTitle.attr("href").split("/")[2].split("#")[0].toLong()

            val eTopicInfo = eCell.selectFirst("span.topic_info")

            val eNode = eTopicInfo.selectFirst("a.node")
            val nodeTitle = eNode.text()
            val nodeName = eNode.attr("href").split("/")[2]

            val userName = eTopicInfo.selectFirst("strong > a").text()

//            val userAvatar = eCell.selectFirst("img.avatar").attr("src")
            val latestReplyTime = eTopicInfo.text().split(" • ")[2]

            val replies = eCell.selectFirst("a.count_livid")?.text()?.toLong() ?: 0

            TopicItem(
                id,
                title,
                nodeName,
                nodeTitle,
                userName,
                "",
                latestReplyTime,
                replies,
                isTop
            )
        }

        return MemberTopics(
            topics, hide, totalCount, currentPage, totalPage
        )
    }

}