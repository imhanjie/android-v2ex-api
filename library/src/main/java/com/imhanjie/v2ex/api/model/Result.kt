package com.imhanjie.v2ex.api.model

data class Result<T>(
    val code: Int,
    val data: T? = null,
    val message: String? = null
) {

    companion object {

        const val CODE_SUCCESS = 1
        const val CODE_FAIL = 0
        const val CODE_USER_EXPIRED = -100

        fun <T> success(data: T): Result<T> =
            Result(
                CODE_SUCCESS,
                data
            )
        fun <T> fail(message: String, code: Int = CODE_FAIL): Result<T> =
            Result(code, null, message)
    }

}