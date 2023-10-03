package kr.co.hyo.domain.post.repository

import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.post.dto.PostCacheDto
import kr.co.hyo.domain.post.entity.PostCache
import kr.co.hyo.domain.post.entity.PostFeed
import kr.co.hyo.domain.post.repository.redistemplate.PostRedisTemplateRepositoryImpl
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
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

@DataRedisTest(properties = ["spring.profiles.active=test"])
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [RedisEmbbededConfig::class, PostRedisTemplateRepositoryImpl::class])
@DisplayName("PostRedisTemplateRepository 단위 테스트")
class PostRedisTemplateRepositoryTests {

    @Autowired
    lateinit var postRedisTemplateRepository: PostRedisTemplateRepository

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @AfterEach
    fun tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }

    @Test
    fun `게시물 피드를 저장한다`() {
        // given
        val memberId = 1L
        val postId = 92783L
        val postFeed = PostFeed(memberId = memberId)
        val key: String = postFeed.getMemberIdPostFeedsKey()
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()

        // when
        postRedisTemplateRepository.zadd(key = key, value = postId, score = score)

        // then
        val postIds: List<Long> =
            postRedisTemplateRepository.zrevRange(key = key, start = 0, end = 10, clazz = Long::class.java)

        assertThat(postIds).isNotEmpty
        assertThat(postIds.first()).isEqualTo(postId)
    }

    @Test
    fun `게시물 캐시를 저장한다`() {
        // given
        val postId: Long = 1
        val memberId: Long = 1
        val postCache = PostCache(postId = postId, memberId = memberId)
        val postKey: String = postCache.getPostKey()
        val expirationTimeMs: Long = postCache.getExpirationTimeMs()
        val value = PostCacheDto(
            postId = postId,
            memberId = 1L,
            title = "title",
            contents = "contents",
            createdDate = LocalDate.now(),
            createdDatetime = LocalDateTime.now(),
            updatedDatetime = LocalDateTime.now(),
        )

        // when
        postRedisTemplateRepository.set(key = postKey, value = value, expirationTimeMs = expirationTimeMs)

        // then
        val postCacheDto: PostCacheDto? =
            postRedisTemplateRepository.get(key = postKey, clazz = PostCacheDto::class.java)

        assertThat(postCacheDto).isNotNull
        assertThat(postCacheDto).isEqualTo(value)
    }
}
