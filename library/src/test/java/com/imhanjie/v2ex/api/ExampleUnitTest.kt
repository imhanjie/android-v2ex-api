package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.parser.Parser
import com.imhanjie.v2ex.api.parser.impl.MemberParser
import com.imhanjie.v2ex.api.support.removeQueryParams
import org.junit.Test
import java.io.File

class ExampleUnitTest {
    @Test
    fun testParser() {
        val html = File("./html/v2ex_member_other.html").readText()
        val parser: Parser = MemberParser()
        println(parser.parser(html))
    }

    @Test
    fun testUtil() {
        val url = "https://v2ex.com/follow/18380?userName=yuanyiz&once=13762"
//        println(extractUrlQueryParams(url))
        println(removeQueryParams(url, "userName"))
    }

}
