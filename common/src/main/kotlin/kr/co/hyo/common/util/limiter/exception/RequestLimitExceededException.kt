package kr.co.hyo.common.util.limiter.exception

data class RequestLimitExceededException(override val message: String): RuntimeException(message)
