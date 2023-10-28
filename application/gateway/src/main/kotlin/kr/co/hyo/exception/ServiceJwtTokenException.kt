package kr.co.hyo.exception

data class ServiceJwtTokenException(override val message: String): RuntimeException(message)
