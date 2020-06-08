package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.Notifications
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup
import kotlin.math.max

object NotificationsParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url.startsWith("${V2ex.BASE_URL}/notifications?p=")
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)

        val items: List<Notifications.Item> = document.selectFirst("#Main")
            .selectFirst("div.box")
            .select("div.cell")
            .filter { it.attr("id").isNotEmpty() }
            .map {
                val id = it.attr("id")
                val userAvatar = it.selectFirst("img").attr("src").replace("_mini", "_large")
                val titleRichContent = it.selectFirst("span.fade").html()
                val replyRichContent = it.selectFirst("div.payload")?.html() ?: ""
                val createTime = it.selectFirst("span.snow").text()

                Notifications.Item(
                    id,
                    userAvatar,
                    titleRichContent,
                    replyRichContent,
                    createTime
                )
            }

        val currentPage = document.selectFirst("a.page_current")?.text()?.toInt() ?: 1
        var totalPage = currentPage
        with(document.select("a.page_normal")!!) {
            if (isNotEmpty()) {
                totalPage = max(last().text().toInt(), currentPage)
            }
        }

        return Notifications(
            items,
            currentPage,
            totalPage
        )
    }

}