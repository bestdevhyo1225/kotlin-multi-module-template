package com.hyoseok.response

data class FailResponse(
    val status: String = "fail",
    val message: String,
)
