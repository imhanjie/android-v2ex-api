package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.parser.impl.MemberRepliesParser
import org.junit.Test
import java.io.File

class ExampleUnitTest {
    @Test
    fun testParser() {
        val html = File("./html/v2ex_member_replies.html").readText()
        val parser: ParserMatcher = MemberRepliesParser()
        println(parser.parser(html))
    }

    @Test
    fun testUtil() {
        val url = "https://v2ex.com/member/Livid/topics"
        println(MATCHER_REGISTRATION.firstOrNull { it.match(url, "get") })
//        println(removeQueryParams(url, "userName"))
    }

}
