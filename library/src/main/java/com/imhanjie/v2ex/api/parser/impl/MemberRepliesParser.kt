package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.MemberReplies
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup
import java.util.regex.Pattern
import kotlin.math.max

object MemberRepliesParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return Pattern.compile("^${V2ex.BASE_URL}/member/\\w+/replies\\?p=\\d+\$").matcher(url).find()
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
        val totalCount = document.selectFirst("div.header").selectFirst("strong.gray")?.text()?.toInt() ?: 0

        val titleElements = document.selectFirst("#Main").selectFirst("div.box").select("div.dock_area").select("td")
        val replyElements = document.selectFirst("#Main").selectFirst("div.box").select("div.reply_content")
        val replies = mutableListOf<MemberReplies.Item>()
        for (i in 0 until replyElements.size) {
            val eTitle = titleElements[i]
            val eReply = replyElements[i]
            val titleRichContent = eTitle.selectFirst("span.gray").html()
            val createTime = eTitle.selectFirst("span.fade").text()
            val replyRichContent = eReply.html()
            replies.add(
                MemberReplies.Item(
                    titleRichContent, createTime, replyRichContent
                )
            )
        }
        return MemberReplies(
            replies, totalCount, currentPage, totalPage
        )
    }

}