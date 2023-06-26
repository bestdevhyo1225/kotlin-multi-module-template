package kr.co.hyo.domain.member.repository

import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.member.entity.MemberToken
import kr.co.hyo.domain.member.repository.redistemplate.MemberTokenRedisTemplateRepositoryImpl
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
@ContextConfiguration(classes = [RedisEmbbededConfig::class, MemberTokenRedisTemplateRepositoryImpl::class])
@DisplayName("MemberTokenRedisTemplateRepository 단위 테스트")
class MemberTokenRedisTemplateRepositoryTests {

    @Autowired
    lateinit var memberTokenRedisTemplateRepository: MemberTokenRedisTemplateRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }

    @Test
    fun `accessToken를 블랙 리스트에 저장한다`() {
        // given
        val memberToken = MemberToken(memberId = 1)
        val key = memberToken.getBlackListTokenKey()
        val blackListToken = "blackListToken"
        val expirationTimeMs = 1000 * 60L

        // when
        memberTokenRedisTemplateRepository.create(key = key, value = blackListToken, expirationTimeMs = expirationTimeMs)

        // then
        val findBlackListToken: String? = memberTokenRedisTemplateRepository.find(key = key)

        assertThat(findBlackListToken).isNotNull()
        assertThat(findBlackListToken).isEqualTo(blackListToken)
    }

    @Test
    fun `refreshToken를 저장한다`() {
        // given
        val memberToken = MemberToken(memberId = 1)
        val key = memberToken.getRefreshTokenKey()
        val refreshToken = "accessToken"
        val expirationTimeMs = 1000 * 60L

        // when
        memberTokenRedisTemplateRepository.create(key = key, value = refreshToken, expirationTimeMs = expirationTimeMs)

        // then
        val findRefreshToken: String? = memberTokenRedisTemplateRepository.find(key = key)

        assertThat(findRefreshToken).isNotNull()
        assertThat(findRefreshToken).isEqualTo(refreshToken)
    }
}
