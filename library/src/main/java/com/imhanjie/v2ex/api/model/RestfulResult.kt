package com.imhanjie.v2ex.api.model

data class RestfulResult<T>(
    var code: Int,
    var data: T? = null,
    var message: String? = null
) {

    companion object {

        const val CODE_SUCCESS = 1
        const val CODE_FAIL = 0
        const val CODE_USER_EXPIRED = -100

        fun <T> success(data: T): RestfulResult<T> =
            RestfulResult(
                CODE_SUCCESS,
                data
            )

        fun <T> fail(message: String, code: Int = CODE_FAIL): RestfulResult<T> =
            RestfulResult(code, null, message)
    }

}