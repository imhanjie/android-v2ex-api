package com.example.v2ex

import android.app.Application
import com.imhanjie.v2ex.api.V2exApi

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        V2exApi.init {
            // 返回在登录接口返回的用户标识 a2Cookie, 没有或未登录可返回 null or ""
            "return your a2Cookie"
        }
    }

}