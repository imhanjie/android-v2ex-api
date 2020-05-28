package com.imhanjie.v2ex.api.support

fun extractUrlQueryParams(url: String): MutableMap<String, Any> {
    val paramsMap = mutableMapOf<String, Any>()
    try {
        val paramsString = url.split("?")[1]
        for (group in paramsString.split("&")) {
            paramsMap[group.split("=")[0]] = group.split("=")[1]
        }
    } catch (ignore: Exception) {
    }
    return paramsMap
}

fun removeQueryParams(url: String, paramName: String): String {
    return try {
        val paramsMap = extractUrlQueryParams(url)
        paramsMap.remove(paramName)
        val sb = StringBuilder(url.split("?")[0])
        if (paramsMap.isNotEmpty()) {
            sb.append("?")
        }
        var count = 1
        for (entry in paramsMap.entries) {
            if (count > 1) {
                sb.append("&")
            }
            sb.append("${entry.key}=${entry.value}")
            count++
        }
        sb.toString()
    } catch (ignore: Exception) {
        ""
    }

}