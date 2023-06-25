package kr.co.hyo.domain.member.repository

import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.member.entity.MemberAuth
import kr.co.hyo.domain.member.repository.v1.MemberAuthRepositoryV1
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@DataRedisTest(properties = ["spring.profiles.active=test"])
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [RedisEmbbededConfig::class, MemberAuthRepositoryV1::class])
@DisplayName("MemberAuthRepository 단위 테스트")
class MemberAuthRepositoryTests {

    @Autowired
    lateinit var memberAuthRepository: MemberAuthRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }

    @Test
    fun `accessToken를 블랙 리스트에 저장한다`() {
        // given
        val memberAuth = MemberAuth(memberId = 1)
        val key = memberAuth.getBlackListTokenKey()
        val blackListToken = "blackListToken"
        val expirationTimeMs = 1000 * 60L

        // when
        memberAuthRepository.create(key = key, value = blackListToken, expirationTimeMs = expirationTimeMs)

        // then
        val findBlackListToken: String? = memberAuthRepository.find(key = key)

        assertThat(findBlackListToken).isNotNull()
        assertThat(findBlackListToken).isEqualTo(blackListToken)
    }

    @Test
    fun `refreshToken를 저장한다`() {
        // given
        val memberAuth = MemberAuth(memberId = 1)
        val key = memberAuth.getRefreshTokenKey()
        val refreshToken = "accessToken"
        val expirationTimeMs = 1000 * 60L

        // when
        memberAuthRepository.create(key = key, value = refreshToken, expirationTimeMs = expirationTimeMs)

        // then
        val findRefreshToken: String? = memberAuthRepository.find(key = key)

        assertThat(findRefreshToken).isNotNull()
        assertThat(findRefreshToken).isEqualTo(refreshToken)
    }
}
