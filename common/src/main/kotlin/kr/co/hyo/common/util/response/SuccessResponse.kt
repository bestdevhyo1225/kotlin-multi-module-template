package kr.co.hyo.common.util.response

data class SuccessResponse<T : Any>(
    val status: String = "success",
    val data: T,
)
