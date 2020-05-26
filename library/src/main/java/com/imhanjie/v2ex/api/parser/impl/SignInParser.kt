package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.model.SignIn
import com.imhanjie.v2ex.api.parser.Parser
import org.jsoup.Jsoup

class SignInParser : Parser {

    override fun parser(html: String): Any {
        val document = Jsoup.parse(html)
        val eTrs = document.selectFirst("#Main").selectFirst("table").select("tr")
        val keyUserName = eTrs[0].selectFirst("input").attr("name")
        val keyPassword = eTrs[1].selectFirst("input").attr("name")
        val keyVerCode = eTrs[2].selectFirst("input").attr("name")
        val verUrlOnce = eTrs[2].selectFirst("div")
            .attr("style")
            .split(";")[0]
            .split("'")[1]
            .split("=")[1]
        return SignIn(
            keyUserName,
            keyPassword,
            keyVerCode,
            verUrlOnce
        )
    }

}