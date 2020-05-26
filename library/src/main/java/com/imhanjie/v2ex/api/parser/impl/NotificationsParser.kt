package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.model.Notifications
import com.imhanjie.v2ex.api.parser.Parser
import org.jsoup.Jsoup
import kotlin.math.max

class NotificationsParser : Parser {

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)

        val items: List<Notifications.Item> = document.selectFirst("#Main")
            .selectFirst("div.box")
            .select("div.cell")
            .filter { it.attr("id").isNotEmpty() }
            .map {
                val userAvatar = it.selectFirst("img").attr("src").replace("_mini", "_large")
                val titleRichContent = it.selectFirst("span.fade").html()
                val replyRichContent = it.selectFirst("div.payload")?.html() ?: ""
                val createTime = it.selectFirst("span.snow").text()

                Notifications.Item(
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