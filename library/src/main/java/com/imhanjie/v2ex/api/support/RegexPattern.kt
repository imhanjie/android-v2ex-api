package com.imhanjie.v2ex.api.support

import java.util.regex.Pattern

object RegexPattern {
    val TOPIC_URL: Pattern = Pattern.compile("^(((http|https)://)?(www.)?v2ex.com)?/t/\\d+((#.*)|(\\?p=\\d*))?\$")
    val IMAGE_URL: Pattern = Pattern.compile("^.*\\.(png|jpg|jpeg|gif)$")
    val PAGE_ONCE: Pattern = Pattern.compile("once=\\d+")
}