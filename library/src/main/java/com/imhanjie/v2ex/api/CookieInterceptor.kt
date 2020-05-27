package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.support.RegexPattern
import okhttp3.*


@SuppressLint("DefaultLocale")
object CookieInterceptor : Interceptor {

    /**
     * 管理每个页面的 PB3_SESSION，当前页面的 once 与 PB3_SESSION 一一对应，
     * 所以某个页面上的操作接口中有 once 码，即请求时需要附带上该 once 所对应的 PB3_SESSION，
     * 下面 map 中的 key 值为 once, value 值为 PB3_SESSION
     */
    private val pb3SessionMap = hashMapOf<String, String>()

    /**
     * once 替换
     */
    fun replaceOnce(oldOnce: String, newOnce: String) {
        val pageSession: String = pb3SessionMap[oldOnce].orEmpty()
        if (pageSession.isNotEmpty()) {
            pb3SessionMap[newOnce] = pageSession
            pb3SessionMap.remove(oldOnce)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = loadForRequest(request)
        val response = chain.proceed(request)
        return saveFromResponse(request, response)
    }

    private fun saveFromResponse(request: Request, response: Response): Response {
        val pb3Session =
            response.headers().values("set-cookie").firstOrNull { it.startsWith("PB3_SESSION=") }
                ?: ""
        if (pb3Session != "") {
            val contentType = response.body()!!.contentType()
            val htmlContent = response.body()!!.string()
            // 将 once 和 PB3_SESSION 对应存储起来
            val matcher = RegexPattern.PAGE_ONCE.matcher(htmlContent)
            if (matcher.find()) {
                val once = matcher.group().split("=")[1]
                pb3SessionMap[once] = pb3Session
            }
            val wrappedBody = ResponseBody.create(contentType, htmlContent)
            return response.newBuilder().body(wrappedBody).build()
        }
        return response
    }

    private fun loadForRequest(request: Request): Request {
        val resultCookies = mutableListOf<Cookie>()

        /**
         * load PB3_SESSION
         */
        var onceString = tryGetOnceFromRequest(request)
        onceString?.let {
            // 根据 once 取出 PB3_SESSION 值
            val pageSession: String = pb3SessionMap[it].orEmpty()
            if (pageSession.isNotEmpty()) {
                resultCookies.add(Cookie.parse(request.url(), pageSession)!!)
            }
        }

        // load A2
        val a2Cookie = V2exApi.a2CookieProvider?.invoke() ?: ""
        if (a2Cookie.isNotEmpty()) {
            Cookie.parse(request.url(), a2Cookie)?.let {
                resultCookies.add(it)
            }
        }

        return if (resultCookies.isEmpty()) {
            request
        } else {
            request.newBuilder().apply {
                header("Cookie", cookieHeader(resultCookies))
            }.build()
        }
    }

    /**
     * 尝试从请求中获取 once
     *
     * 1. 首先尝试从 url 中寻找 once
     * 2. 其次从 requestBody 中寻找 once
     */
    fun tryGetOnceFromRequest(request: Request): String? {
        var onceString = ""
        var matcher = RegexPattern.PAGE_ONCE.matcher(request.url().toString())
        if (matcher.find()) {
            onceString = matcher.group()
        } else {
            val buffer = okio.Buffer();
            request.body()?.writeTo(buffer)
            val requestParam = buffer.readUtf8()
            matcher = RegexPattern.PAGE_ONCE.matcher(requestParam)
            if (matcher.find()) {
                onceString = matcher.group()
            }
        }
        return if (onceString.isNotEmpty()) {
            onceString.split("=")[1]
        } else {
            null
        }
    }

    /** Returns a 'Cookie' HTTP request header with all cookies, like `a=b; c=d`.  */
    private fun cookieHeader(cookies: List<Cookie>): String {
        val cookieHeader = StringBuilder()
        var i = 0
        val size = cookies.size
        while (i < size) {
            if (i > 0) {
                cookieHeader.append("; ")
            }
            val cookie = cookies[i]
            cookieHeader.append(cookie.name()).append('=').append(cookie.value())
            i++
        }
        return cookieHeader.toString()
    }

}