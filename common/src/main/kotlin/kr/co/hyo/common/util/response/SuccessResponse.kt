package com.hyoseok.response

data class SuccessResponse<T : Any>(
    val status: String = "success",
    val data: T,
)
