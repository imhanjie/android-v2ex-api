package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.support.V2exConstants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 用于某些接口请求时需要添加一些动态 Header
 */
class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestUrl = request.url().toString()

        // 收藏 & 取消收藏主题
        if (requestUrl.startsWith("${V2exConstants.BASE_URL}/favorite/topic/") || requestUrl.startsWith("${V2exConstants.BASE_URL}/unfavorite/topic/")) {
            val topicId = requestUrl.split("?")[0].split("/").last()
            request = request.newBuilder().apply {
                header("Referer", "${V2exConstants.BASE_URL}/t/$topicId")
            }.build()
        }

        return chain.proceed(request)
    }

}