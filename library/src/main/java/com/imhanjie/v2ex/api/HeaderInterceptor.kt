package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.support.V2exConstants
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 用于某些接口请求时需要添加一些动态 Header
 */
class HeaderInterceptor : Interceptor {

    @SuppressLint("DefaultLocale")
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestUrl = request.url().toString().replace(V2exConstants.BASE_URL, "")

        // 收藏 & 取消收藏主题
        request =
            if (requestUrl.startsWith("/favorite/topic/") || requestUrl.startsWith("/unfavorite/topic/")) {
                val topicId = requestUrl.split("?")[0].split("/").last()
                request.newBuilder().apply {
                    header("Referer", "${V2exConstants.BASE_URL}/t/$topicId")
                }.build()
            } else if (requestUrl.startsWith("/_captcha")) {
                request.newBuilder().apply {
                    header("Referer", "${V2exConstants.BASE_URL}/signin")
                }.build()
            } else if (requestUrl.startsWith("/signin") && request.method().toUpperCase() == "POST") {
                request.newBuilder().apply {
                    header("Referer", "${V2exConstants.BASE_URL}/signin")
                }.build()
            } else if (requestUrl.startsWith("/favorite/node/") || requestUrl.startsWith("/unfavorite/node/")) {
                request.newBuilder().apply {
                    header("Referer", "${V2exConstants.BASE_URL}/go")
                }.build()
            } else {
                request
            }
        return chain.proceed(request)
    }

}