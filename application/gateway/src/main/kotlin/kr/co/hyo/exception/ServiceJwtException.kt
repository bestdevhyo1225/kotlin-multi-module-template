package kr.co.hyo.exception

data class ServiceJwtException(override val message: String): RuntimeException(message)
