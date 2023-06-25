package kr.co.hyo.util.jwt

import kr.co.hyo.common.util.jwt.JwtCreateHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("JwtCreateHelper 단위 테스트")
class JwtCreateHelperTests {

    @Test
    fun `AccessToken을 생성한다`() {
        // given
        val expirationTimeMs: Long = 1000 * 60 // 1분
        val refreshExpirationTimeMs: Long = 1000 * 60 * 60 // 1시간
        val jwtCreateHelper =
            JwtCreateHelper(
                secretKey = "jwtCreateHelperTestsJwtSecretKey",
                expirationTimeMs = expirationTimeMs,
                refreshExpirationTimeMs = refreshExpirationTimeMs,
            )
        val claims: Map<String, Any> = mapOf("memberId" to 1L)

        // when
        val accessToken: String = jwtCreateHelper.createAccessToken(claims = claims)

        // then
        assertThat(accessToken).isNotBlank()
    }

    @Test
    fun `RefreshToken을 생성한다`() {
        // given
        val expirationTimeMs: Long = 1000 * 60 // 1분
        val refreshExpirationTimeMs: Long = 1000 * 60 * 60 // 1시간
        val jwtCreateHelper =
            JwtCreateHelper(
                secretKey = "jwtCreateHelperTestsJwtSecretKey",
                expirationTimeMs = expirationTimeMs,
                refreshExpirationTimeMs = refreshExpirationTimeMs,
            )
        val claims: Map<String, Any> = mapOf("memberId" to 1L)

        // when
        val refreshToken: String = jwtCreateHelper.createRefreshToken(claims = claims)

        // then
        assertThat(refreshToken).isNotBlank()
    }
}
