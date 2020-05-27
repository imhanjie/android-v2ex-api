package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.parser.Parser
import com.imhanjie.v2ex.api.parser.impl.TopicParser
import org.junit.Test
import java.io.File

class ExampleUnitTest {
    @Test
    fun testParser() {
        val html = File("./html/v2ex_topic.html").readText()
        val parser: Parser = TopicParser()
        println(parser.parser(html))
    }

}
