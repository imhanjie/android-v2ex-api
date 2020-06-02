package com.imhanjie.v2ex.api.parser.impl

import com.imhanjie.v2ex.api.model.NavNode
import com.imhanjie.v2ex.api.model.TinyNode
import com.imhanjie.v2ex.api.parser.Parser
import org.jsoup.Jsoup

//class NavNodeParser : Parser {
//
//    override fun parser(html: String): List<NavNode> {
//        val document = Jsoup.parse(html)
//        return document.selectFirst("#Main").select("div.box").last().select("tbody").map {
//            val eTd = it.select("td")
//            val type = eTd[0].selectFirst("span.fade").text()
//            val children = eTd[1].select("a").map { aE ->
//                val nodeTitle = aE.text()
//                val nodeName = aE.attr("href").split("/")[2]
//                TinyNode(nodeTitle, nodeName)
//            }
//            NavNode(type, children)
//        }
//    }
//
//}