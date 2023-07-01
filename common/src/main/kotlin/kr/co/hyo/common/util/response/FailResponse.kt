package kr.co.hyo.common.util.response

data class FailResponse(
    val status: String = "fail",
    val message: String,
)
