package com.imhanjie.v2ex.api.support

import com.imhanjie.v2ex.api.model.Result
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

fun Response.recreateSuccessJsonResponse(data: Any): Response {
    val responseBody = ResponseBody.create(
        MediaType.parse("application/json;charset=UTF-8"),
        parseToJson(Result.success(data))
    )
    return this.newBuilder().code(200).body(responseBody).build()
}

fun Response.recreateFailJsonResponse(message: String, code: Int = Result.CODE_FAIL): Response {
    val responseBody = ResponseBody.create(
        MediaType.parse("application/json;charset=UTF-8"),
        parseToJson(Result.fail<String>(message, code))
    )
    return this.newBuilder().code(200).body(responseBody).build()
}