package kr.co.hyo.exception

data class InvalidJwtTokenException(override val message: String): RuntimeException(message)
