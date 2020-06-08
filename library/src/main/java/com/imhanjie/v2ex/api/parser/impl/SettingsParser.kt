package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.MyUserInfo
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object SettingsParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/settings/privacy"
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val eCell = document.selectFirst("#Rightbar")
            .selectFirst("div.box")
            .selectFirst("div.cell")
        val avatar = eCell.selectFirst("img.avatar").attr("src")
        val userName = eCell.selectFirst("span.bigger").text()
        val eCounts = eCell.select("table")[1].select("span.bigger")

        val eMoney = document.selectFirst("#money").selectFirst("a")
        val moneyGold = eMoney.text().split(" ")[0].toLong()
        val moneySilver = eMoney.text().split(" ")[1].toLong()
        val moneyBronze = eMoney.text().split(" ")[2].toLong()

        return MyUserInfo(
            userName,
            avatar,
            eCounts[0].text().toLong(),
            eCounts[1].text().toLong(),
            eCounts[2].text().toLong(),
            moneyGold,
            moneySilver,
            moneyBronze
        )
    }

}