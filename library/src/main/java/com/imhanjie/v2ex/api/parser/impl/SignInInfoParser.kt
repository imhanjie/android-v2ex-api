package com.imhanjie.v2ex.api.parser.impl

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.ParserMatcher
import com.imhanjie.v2ex.api.model.SignInInfo
import com.imhanjie.v2ex.api.support.V2ex
import org.jsoup.Jsoup

object SignInInfoParser : ParserMatcher {

    @SuppressLint("DefaultLocale")
    override fun match(url: String, method: String): Boolean {
        return url == "${V2ex.BASE_URL}/signin" && method.toUpperCase() == "GET"
    }

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
        return SignInInfo(
            keyUserName,
            keyPassword,
            keyVerCode,
            verUrlOnce
        )
    }

}