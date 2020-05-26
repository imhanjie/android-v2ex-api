package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.v2ex.parser.Parser
import com.imhanjie.v2ex.parser.impl.*
import com.imhanjie.v2ex.parser.model.Result
import com.imhanjie.v2ex.parser.model.V2exResult
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ParserInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var url = request.url().toString()
        val originMethod = request.method()
        var response = chain.proceed(request)

        var isFailRequest = false

        // 单独处理重定向逻辑
        if (response.code() == 302) {
            url = response.header("location") ?: ""
            if (url == "/signin/cooldown") {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(ApiServer.BASE_URL + url)
                    .get()
                    .build()
                isFailRequest = true
                response = chain.proceed(redirectRequest)
            } else if (url.startsWith("/signin?next=")) {
                // 登录信息失效，直接返回
                return response.recreateFailJsonResponse("请先登录后再进行查看", Result.CODE_USER_EXPIRED)
            } else if (url.startsWith("${ApiServer.BASE_URL}/go")) {
                // 收藏 / 取消收藏成功
                return response.recreateSuccessJsonResponse("")
            }
        } else if (response.code() == 403) {
            // IP 被(临时)禁
            return response.recreateFailJsonResponse("403 请稍后再试")
        }

        val targetParser = getParser(originMethod, url)
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

    /**
     * 根据 url 获取 html 解析器
     *
     * @param url 请求的 url
     * @return 若返回 null 说明不需要解析，反之需要解析
     */
    @SuppressLint("DefaultLocale")
    private fun getParser(method: String, url: String): Parser? {
        return with(url) {
            if (startsWith("${ApiServer.BASE_URL}/recent?p=")
                || startsWith("${ApiServer.BASE_URL}/?tab=")
            ) {
                LatestTopicsParser()
            } else if (startsWith("${ApiServer.BASE_URL}/t/")) {
                TopicParser()
            } else if (startsWith("${ApiServer.BASE_URL}/go/")) {
                NodeTopicsParser()
            } else if (equals("${ApiServer.BASE_URL}/signin") && method.toUpperCase() == "GET") {
                SignInParser()
            } else if (equals("/signin/cooldown")) {
                CoolDownParser()
            } else if (equals("${ApiServer.BASE_URL}/settings")) {
                SettingsParser()
            } else if (equals("${ApiServer.BASE_URL}/planes")) {
                NodeParser()
            } else if (equals("${ApiServer.BASE_URL}/my/nodes")) {
                MyNodesParser()
            } else if (startsWith("${ApiServer.BASE_URL}/notifications?p=")) {
                NotificationsParser()
            } else if (startsWith("${ApiServer.BASE_URL}/thank/reply/")) {
                V2exResultParser()
            } else {
                null
            }
        }
    }

}