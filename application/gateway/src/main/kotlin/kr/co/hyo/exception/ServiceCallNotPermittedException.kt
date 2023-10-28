package kr.co.hyo.exception

data class ServiceCallNotPermittedException(override val message: String) : RuntimeException(message)
