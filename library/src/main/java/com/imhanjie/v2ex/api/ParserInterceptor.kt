package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.model.RestfulResult
import com.imhanjie.v2ex.api.model.V2exResult
import com.imhanjie.v2ex.api.parser.impl.*
import com.imhanjie.v2ex.api.support.V2ex
import com.imhanjie.v2ex.api.support.recreateFailJsonResponse
import com.imhanjie.v2ex.api.support.recreateSuccessJsonResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 匹配器注册
 */
val MATCHER_REGISTRATION = listOf<ParserMatcher>(
    CoolDownParser,
    LatestTopicsParser,
    MemberParser,
    MemberRepliesParser,
    MemberTopicsParser,
    MyFavoriteTopicsParser,
    MyNodesParser,
    NodeParser,
    NodeTopicsParser,
    NotificationsParser,
    SettingsParser,
    SignInParser,
    TopicParser,
    V2exResultParser
)

class ParserInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var requestUrl = request.url().toString()
        val originMethod = request.method()
        var response = chain.proceed(request)

        var isFailRequest = false

        // 单独处理重定向逻辑
        if (response.code() < 200 || response.code() >= 300) {
            if (response.code() == 302) {
                var redirectUrl = response.header("location") ?: ""
                if (!redirectUrl.startsWith(V2ex.BASE_URL)) {
                    redirectUrl = V2ex.BASE_URL + redirectUrl
                }
                if (redirectUrl == "${V2ex.BASE_URL}/signin/cooldown") {
                    response.close()
                    val redirectRequest = Request.Builder()
                        .url(redirectUrl)
                        .get()
                        .build()
                    isFailRequest = true
                    response = chain.proceed(redirectRequest)
                    requestUrl = redirectUrl
                } else if (
                    requestUrl.startsWith("${V2ex.BASE_URL}/favorite/topic/")
                    || requestUrl.startsWith("${V2ex.BASE_URL}/unfavorite/topic/")
                    || requestUrl.startsWith("${V2ex.BASE_URL}/follow/")
                    || requestUrl.startsWith("${V2ex.BASE_URL}/unfollow/")
                    || requestUrl.startsWith("${V2ex.BASE_URL}/block/")
                    || requestUrl.startsWith("${V2ex.BASE_URL}/unblock/")
                ) {
                    response.close()
                    val redirectRequest = Request.Builder()
                        .url(redirectUrl)
                        .get()
                        .build()
                    response = chain.proceed(redirectRequest)
                    requestUrl = redirectUrl
                } else if (redirectUrl.startsWith("${V2ex.BASE_URL}/signin?next=")) {
                    // 登录信息失效，直接返回
                    return response.recreateFailJsonResponse("请先登录后再进行查看", RestfulResult.CODE_USER_EXPIRED)
                } else {
                    // 其余重定向一律视为请求成功
                    return response.recreateSuccessJsonResponse("")
                }
            } else {
                return response.recreateFailJsonResponse("${response.code()} 请稍后再试")
            }
        }

        val targetParser = MATCHER_REGISTRATION.firstOrNull { it.match(requestUrl, originMethod) }
        if (targetParser == null) {
            // 未发现 html 解析器，说明不需要解析，直接返回 response
            return response
        } else {
            // 需要进行 html 解析
            val html = response.body()!!.string()
            val obj: Any
            return try {
                obj = targetParser.parser(html)
                if (obj is V2exResult) {    // v2ex json result 转成项目 result
                    // 替换 once 缓存
                    val oldOnce = CookieInterceptor.tryGetOnceFromRequest(request)
                    oldOnce?.let {
                        CookieInterceptor.replaceOnce(it, obj.once)
                    }
                    response.recreateSuccessJsonResponse(obj)
                } else if (isFailRequest) {
                    response.recreateFailJsonResponse(obj.toString())
                } else {
                    response.recreateSuccessJsonResponse(obj)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                response.recreateFailJsonResponse("数据解析错误")
            }
        }
    }

}