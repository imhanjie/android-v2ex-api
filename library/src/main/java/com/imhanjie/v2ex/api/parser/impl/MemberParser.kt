package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.Member
import com.imhanjie.v2ex.api.support.RegexPattern
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup
import java.util.regex.Pattern

object MemberParser : ParserMatcher {

    override fun match(url: String, method: String): Boolean {
        return Pattern.compile("^${V2ex.BASE_URL}/member/\\w+\$").matcher(url).find()
    }

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val eMember = document.selectFirst("#Main").selectFirst("div.box")

        val userName = eMember.selectFirst("h1").text()
        val avatar = eMember.selectFirst("img.avatar").attr("src")
        val id = eMember.selectFirst("span.gray").text().split("，")[0].split(" ")[2].toLong()
        val createTime = eMember.selectFirst("span.gray").textNodes()[0].text().split("，")[1].replace("加入于 ", "")

        var isFollowing = false
        var isBlock = false
        var blockParamT = ""

        val eButton = eMember.selectFirst("div.fr").select("input")
        if (eButton.size == 2) {
            isFollowing = eButton[0].attr("value") == "取消特别关注"
            isBlock = eButton[1].attr("value") == "Unblock"
            blockParamT = eButton[1].attr("onclick").split("=").last().split("'")[0]
        }

        var isMe = false
        try {
            isMe = userName == document.selectFirst("#Top").select("td")[2].select("a")[1].text()
        } catch (ignore: Exception) {
        }

        var once = ""
        val onceMatcher = RegexPattern.PAGE_ONCE.matcher(html)
        if (onceMatcher.find()) {
            once = onceMatcher.group().split("=")[1]
        }

        return Member(
            id,
            userName,
            avatar,
            isFollowing,
            isBlock,
            createTime,
            isMe,
            once,
            blockParamT
        )
    }

}