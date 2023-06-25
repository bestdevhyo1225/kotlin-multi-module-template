package kr.co.hyo.util.jwt

import kr.co.hyo.common.util.jwt.JwtCreateHelper
import kr.co.hyo.common.util.jwt.JwtParseHelper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("JwtParseHelper 단위 테스트")
class JwtParseHelperTests {

    @Test
    fun `accessToken에서 memberId를 가져온다`() {
        // given
        val secreyKey = "jwtCreateHelperTestsJwtSecretKey"
        val expirationTimeMs: Long = 1000 * 60 // 1분
        val refreshExpirationTimeMs: Long = 1000 * 60 * 60 // 1시간
        val jwtCreateHelper =
            JwtCreateHelper(
                secretKey = "jwtCreateHelperTestsJwtSecretKey",
                expirationTimeMs = expirationTimeMs,
                refreshExpirationTimeMs = refreshExpirationTimeMs,
            )
        val claims: Map<String, Any> = mapOf("memberId" to 1L)
        val accessToken: String = jwtCreateHelper.createAccessToken(claims = claims)
        val jwtParseHelper = JwtParseHelper(secretKey = secreyKey)

        // when
        val memberId: String = jwtParseHelper.getValue(accessToken = accessToken, key = "memberId")

        // then
        assertThat(memberId.toLong()).isEqualTo(1L)
    }

    @Test
    fun `accessToken 상태가 유효하면, 사용이 가능하다`() {
        // given
        val secreyKey = "jwtCreateHelperTestsJwtSecretKey"
        val expirationTimeMs: Long = 1000 * 60 // 1분
        val refreshExpirationTimeMs: Long = 1000 * 60 * 60 // 1시간
        val jwtCreateHelper =
            JwtCreateHelper(
                secretKey = "jwtCreateHelperTestsJwtSecretKey",
                expirationTimeMs = expirationTimeMs,
                refreshExpirationTimeMs = refreshExpirationTimeMs,
            )
        val claims: Map<String, Any> = mapOf("memberId" to 1L)
        val accessToken: String = jwtCreateHelper.createAccessToken(claims = claims)
        val jwtParseHelper = JwtParseHelper(secretKey = secreyKey)

        // when, then
        assertThatCode { jwtParseHelper.verify(accessToken = accessToken) }.doesNotThrowAnyException()
    }
}
