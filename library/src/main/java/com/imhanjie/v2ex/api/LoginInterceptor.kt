package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.v2ex.api.model.LoginInfo
import com.imhanjie.v2ex.api.parser.impl.SignInProblemParser
import com.imhanjie.v2ex.api.support.V2ex
import com.imhanjie.v2ex.api.support.recreateFailJsonResponse
import com.imhanjie.v2ex.api.support.recreateSuccessJsonResponse
import okhttp3.Interceptor
import okhttp3.Response

class LoginInterceptor : Interceptor {

    @SuppressLint("DefaultLocale")
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val method = request.method()

        /**
         * GET /signin 接口中不能携带 cookies
         */
        val requestUrl = request.url().toString()
        if (requestUrl == "${V2ex.BASE_URL}/signin" && method.toUpperCase() == "GET") {
            request = request.newBuilder().apply { removeHeader("cookie") }.build()
        }

        val response = chain.proceed(request)

        val responseUrl = response.request().url().toString()
        if (responseUrl != "${V2ex.BASE_URL}/signin" || method.toUpperCase() != "POST") {
            return response
        }
        // 根据响应头中是否有 "A2" cookie 来判定是否登录成功
        val a2Cookie: String? =
            response.headers().values("set-cookie").firstOrNull { it.startsWith("A2=") }
        return if (a2Cookie != null) {
            // 登录成功
            response.recreateSuccessJsonResponse(LoginInfo(a2Cookie))
        } else {
            // 登陆失败
            try {
                val html = response.body()!!.string()
                response.recreateFailJsonResponse(SignInProblemParser.parser(html).toString())
            } catch (e: Exception) {
                response.recreateFailJsonResponse("登录数据解析错误")
            }
        }
    }

}