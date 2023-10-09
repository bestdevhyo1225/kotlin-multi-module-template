package kr.co.hyo.common.util.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

class JwtParseHelper(
    private val secretKey: String,
) {

    fun verify(accessToken: String) {
        val expirationDate: Date = getClaims(accessToken).expiration
        val currentDate = Date()
        val isValid: Boolean = try {
            currentDate.before(expirationDate)
        } catch (exception: NullPointerException) {
            throw IllegalArgumentException("유효하지 않은 엑세스 토큰")
        }
        require(isValid) { "유효하지 않은 엑세스 토큰" }
    }

    fun getValue(accessToken: String, key: String): String {
        val value: Any = getClaims(accessToken)[key]
            ?: throw IllegalArgumentException("Claims 객체에 $key 값이 존재하지 않습니다.")

        return value.toString()
    }

    fun getExpirationTimeMs(accessToken: String): Long = getClaims(accessToken).expiration.time

    private fun getClaims(accessToken: String): Claims {
        val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
        val jwtParser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()

        return try {
            jwtParser.parseClaimsJws(accessToken).body
        } catch (exception: UnsupportedJwtException) {
            throw IllegalArgumentException(exception.localizedMessage)
        } catch (exception: MalformedJwtException) {
            throw IllegalArgumentException(exception.localizedMessage)
        } catch (exception: SignatureException) {
            throw IllegalArgumentException(exception.localizedMessage)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException(exception.localizedMessage)
        } catch (exception: ExpiredJwtException) {
            throw IllegalArgumentException(exception.localizedMessage)
        }
    }
}
