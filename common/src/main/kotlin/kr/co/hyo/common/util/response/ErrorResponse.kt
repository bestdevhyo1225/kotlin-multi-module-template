package com.hyoseok.response

data class ErrorResponse(
    val status: String = "error",
    val message: String,
)
