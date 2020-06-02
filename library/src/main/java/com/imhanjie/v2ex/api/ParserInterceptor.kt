package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.api.model.RestfulResult
import com.imhanjie.v2ex.api.model.V2exResult
import com.imhanjie.v2ex.api.parser.impl.*
import com.imhanjie.v2ex.api.support.V2exConstants
import com.imhanjie.v2ex.api.support.recreateFailJsonResponse
import com.imhanjie.v2ex.api.support.recreateSuccessJsonResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 匹配器注册
 */
val MATCHER_REGISTRATION = listOf<ParserMatcher>(
    CoolDownParser(),
    LatestTopicsParser(),
    MemberParser(),
    MemberRepliesParser(),
    MemberTopicsParser(),
    MyFavoriteTopicsParser(),
    MyNodesParser(),
    NodeParser(),
    NodeTopicsParser(),
    NotificationsParser(),
    SettingsParser(),
    SignInParser(),
    TopicParser(),
    V2exResultParser()
)

class ParserInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var requestUrl = request.url().toString()
        val originMethod = request.method()
        var response = chain.proceed(request)

        var isFailRequest = false

        // 单独处理重定向逻辑
        if (response.code() == 302) {
            var redirectUrl = response.header("location") ?: ""
            if (redirectUrl == "/signin/cooldown") {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(V2exConstants.BASE_URL + redirectUrl)
                    .get()
                    .build()
                isFailRequest = true
                response = chain.proceed(redirectRequest)
                requestUrl = redirectUrl
            } else if (redirectUrl.startsWith("/signin?next=")) {
                // 登录信息失效，直接返回
                return response.recreateFailJsonResponse("请先登录后再进行查看", RestfulResult.CODE_USER_EXPIRED)
            } else if (requestUrl.startsWith("${V2exConstants.BASE_URL}/favorite/topic/") || requestUrl.startsWith("${V2exConstants.BASE_URL}/unfavorite/topic/")) {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(redirectUrl)
                    .get()
                    .build()
                response = chain.proceed(redirectRequest)
                requestUrl = redirectUrl
            } else if (requestUrl.startsWith("${V2exConstants.BASE_URL}/follow/") || requestUrl.startsWith("${V2exConstants.BASE_URL}/unfollow/")) {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(redirectUrl)
                    .get()
                    .build()
                response = chain.proceed(redirectRequest)
                requestUrl = redirectUrl
            } else if (requestUrl.startsWith("${V2exConstants.BASE_URL}/block/") || requestUrl.startsWith("${V2exConstants.BASE_URL}/unblock/")) {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(V2exConstants.BASE_URL + redirectUrl)
                    .get()
                    .build()
                response = chain.proceed(redirectRequest)
                requestUrl = V2exConstants.BASE_URL + redirectUrl
            } else {
                // 其余重定向一律视为请求成功
                return response.recreateSuccessJsonResponse("")
            }
        } else if (response.code() == 403) {
            // IP 被(临时)禁
            return response.recreateFailJsonResponse("403 请稍后再试")
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

//    @SuppressLint("DefaultLocale")
//    private fun getParser(method: String, url: String): Parser? {
//        return with(url) {
//            if (startsWith("${V2exConstants.BASE_URL}/recent?p=")
//                || startsWith("${V2exConstants.BASE_URL}/?tab=")
//            ) {
//                LatestTopicsParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/t/")) {
//                TopicParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/go/")) {
//                NodeTopicsParser()
//            } else if (equals("${V2exConstants.BASE_URL}/signin") && method.toUpperCase() == "GET") {
//                SignInParser()
//            } else if (equals("/signin/cooldown")) {
//                CoolDownParser()
//            } else if (equals("${V2exConstants.BASE_URL}/settings")) {
//                SettingsParser()
//            } else if (equals("${V2exConstants.BASE_URL}/planes")) {
//                NodeParser()
//            } else if (equals("${V2exConstants.BASE_URL}/my/nodes")) {
//                MyNodesParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/notifications?p=")) {
//                NotificationsParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/thank/reply/")) {
//                V2exResultParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/my/topics?p=")) {
//                MyFavoriteTopicsParser()
//            } else if (startsWith("${V2exConstants.BASE_URL}/member/")) {
//                MemberParser()
//            } else {
//                null
//            }
//        }
//    }

}