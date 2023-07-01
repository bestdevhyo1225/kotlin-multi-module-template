package kr.co.hyo.common.util.response

data class ErrorResponse(
    val status: String = "error",
    val message: String,
)
