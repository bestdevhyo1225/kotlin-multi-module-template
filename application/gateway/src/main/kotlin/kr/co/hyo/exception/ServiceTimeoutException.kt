package kr.co.hyo.exception

data class ServiceTimeoutException(override val message: String) : RuntimeException(message)
