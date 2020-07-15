package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.Reply
import com.imhanjie.v2ex.api.model.Topic
import com.imhanjie.v2ex.api.support.RegexPattern
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.math.max

object TopicParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2ex.BASE_URL}/t/")
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)

        val id = document.selectFirst("div.votes").attr("id").split("_")[1].toLong()
        val title = document.selectFirst("#Main").selectFirst("div.header").selectFirst("h1").text()

        var nodeName = ""
        var nodeTitle = ""
        for (ae in document.selectFirst("#Main").selectFirst("div.header").select("a")) {
            val href = ae.attr("href")
            val key = "/go/"
            if (href.isNotEmpty() && href.startsWith(key)) {
                nodeName = href.split(key)[1]
                nodeTitle = ae.text()
                break
            }
        }

        val userAvatar =
            document.selectFirst("#Main").selectFirst("div.box").selectFirst("div.fr").selectFirst("img.avatar").attr("src")
        val userName: String
        val createTime: String
        val click: Long
        with(document.selectFirst("small.gray")) {
            userName = text().split(" · ")[0]
            createTime = text().split(" · ")[1]
            click = text().split(" · ")[2].split(" ")[0].toLong()
        }

        val richContent = (document.selectFirst("div.markdown_body")?.html() ?: document.selectFirst("div.topic_content")?.html()) ?: ""

        val currentPage = document.selectFirst("a.page_current")?.text()?.toInt() ?: 1
        var totalPage = currentPage
        with(document.select("a.page_normal")!!) {
            if (isNotEmpty()) {
                totalPage = max(last().text().toInt(), currentPage)
            }
        }

        // 附言
        var subtleNo = 1
        val subtleList = document.select("div.subtle").map {
            val subtleCreateTime = it.selectFirst("span.fade").text().split(" · ")[1]
            val subtleRichContent = it.selectFirst("div.topic_content").html()
            Topic.Subtle(
                subtleNo++,
                subtleCreateTime,
                subtleRichContent
            )
        }

        var once = ""
        val onceMatcher = RegexPattern.PAGE_ONCE.matcher(html)
        if (onceMatcher.find()) {
            once = onceMatcher.group().split("=")[1]
        }

        val rightCell = document.selectFirst("#Rightbar")
            .selectFirst("div.box")
            .selectFirst("div.cell")
        val isMyTopic = userName == rightCell.selectFirst("span.bigger")?.text()

        var favoriteParam = ""
        var isFavorite = false
        document.selectFirst("#Main").selectFirst("div.topic_buttons")?.selectFirst("a.tb")?.let {
            favoriteParam = it.attr("href").split("=")[1]
            isFavorite = it.text() == "取消收藏"
        }

        val canAppend = document.selectFirst("#Main").selectFirst("div.header").select("a").firstOrNull { it.text() == "APPEND" } != null

        val replies: List<Reply> = parserReplies(document)
        return Topic(
            id,
            title,
            nodeName,
            nodeTitle,
            userName,
            userAvatar,
            createTime,
            click,
            richContent,
            subtleList,
            replies,
            currentPage,
            totalPage,
            once,
            isMyTopic,
            favoriteParam,
            isFavorite,
            canAppend
        )
    }

    private fun parserReplies(document: Document): List<Reply> {
        return document.select("#Main").select("div.cell")
            .filter { eCell ->
                val attrId = eCell.attr("id")
                attrId.isNotEmpty() && attrId.startsWith("r_")
            }
            .map { eCell ->
                val replyId = eCell.attr("id").split("_")[1].toLong()
                val userAvatar = eCell.selectFirst("img.avatar").attr("src")
                val userName = eCell.selectFirst("a.dark").text()
                val content = eCell.selectFirst("div.reply_content").html()
                val time = eCell.selectFirst("span.ago").text()
                val thankCount = eCell.selectFirst("span.small.fade")?.text()?.toLong() ?: 0
                val thanked = (eCell.selectFirst("div.thank_area")?.selectFirst("div.thanked")) != null
                val no = eCell.selectFirst("span.no").text().toLong()
                Reply(
                    replyId, userAvatar, userName, content, time, thankCount, thanked, no
                )
            }
    }

}