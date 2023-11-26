package kr.co.hyo.common.util.limiter

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class RequestLimiter(
    val count: Long = 50,
)
