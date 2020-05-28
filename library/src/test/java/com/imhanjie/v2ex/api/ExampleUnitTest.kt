package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.parser.Parser
import com.imhanjie.v2ex.api.parser.impl.MyFavoriteTopicsParser
import org.junit.Test
import java.io.File

class ExampleUnitTest {
    @Test
    fun testParser() {
        val html = File("./html/v2ex_my_favorite_topics.html").readText()
        val parser: Parser = MyFavoriteTopicsParser()
        println(parser.parser(html))
    }

}
