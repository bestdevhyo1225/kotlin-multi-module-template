package kr.co.hyo.exception

data class ServiceConnectException(override val message: String) : RuntimeException(message)
