package kr.co.hyo.config

import kr.co.hyo.common.util.jwt.JwtParseHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
) {

    @Bean
    fun jwtParseHelper(): JwtParseHelper {
        return JwtParseHelper(secretKey = secretKey)
    }
}
