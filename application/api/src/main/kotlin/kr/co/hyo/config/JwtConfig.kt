package kr.co.hyo.config

import kr.co.hyo.common.util.jwt.JwtCreateHelper
import kr.co.hyo.common.util.jwt.JwtParseHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,

    @Value("\${jwt.expiration-time-ms}")
    private val expirationTimeMs: Long,

    @Value("\${jwt.refresh-expiration-time-ms}")
    private val refreshExpirationTimeMs: Long,
) {

    @Bean
    fun jwtCreateHelper(): JwtCreateHelper {
        return JwtCreateHelper(
            secretKey = secretKey,
            expirationTimeMs = expirationTimeMs,
            refreshExpirationTimeMs = refreshExpirationTimeMs,
        )
    }

    @Bean
    fun jwtParseHelper(): JwtParseHelper {
        return JwtParseHelper(secretKey = secretKey)
    }
}
