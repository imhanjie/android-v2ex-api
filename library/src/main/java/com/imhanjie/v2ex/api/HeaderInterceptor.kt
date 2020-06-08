package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.support.V2ex
import com.imhanjie.v2ex.api.support.extractUrlQueryParams
import com.imhanjie.v2ex.api.support.removeQueryParams
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 用于某些接口请求时需要添加一些动态 Header
 */
class HeaderInterceptor : Interceptor {

    @SuppressLint("DefaultLocale")
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val fullRequestUrl = request.url().toString()
        val requestUrl = request.url().toString().replace(V2ex.BASE_URL, "")

        // 收藏 & 取消收藏主题
        request =
            if (requestUrl.startsWith("/favorite/topic/") || requestUrl.startsWith("/unfavorite/topic/")) {
                val topicId = requestUrl.split("?")[0].split("/").last()
                request.newBuilder().apply {
                    header("Referer", "${V2ex.BASE_URL}/t/$topicId")
                }.build()
            } else if (requestUrl.startsWith("/_captcha")) {
                request.newBuilder().apply {
                    header("Referer", "${V2ex.BASE_URL}/signin")
                }.build()
            } else if (requestUrl.startsWith("/signin") && request.method().toUpperCase() == "POST") {
                request.newBuilder().apply {
                    header("Referer", "${V2ex.BASE_URL}/signin")
                }.build()
            } else if (requestUrl.startsWith("/favorite/node/") || requestUrl.startsWith("/unfavorite/node/")) {
                request.newBuilder().apply {
                    header("Referer", "${V2ex.BASE_URL}/go")
                }.build()
            } else if (requestUrl.startsWith("/follow/") || requestUrl.startsWith("/unfollow/")
                || requestUrl.startsWith("/block/") || requestUrl.startsWith("/unblock/")
            ) {
                // https://v2ex.com/follow/1?once=14470&userName=Livid (ps: userName 给服务器之前需要移除掉)
                // https://v2ex.com/block/1?t=1123456789&userName=Livid (ps: userName 给服务器之前需要移除掉)
                val paramsMap = extractUrlQueryParams(fullRequestUrl)
                val userName = paramsMap["userName"]
                request.newBuilder().apply {
                    url(removeQueryParams(fullRequestUrl, "userName"))
                    header("Referer", "${V2ex.BASE_URL}/member/$userName")
                }.build()
            } else {
                request
            }
        return chain.proceed(request)
    }

}