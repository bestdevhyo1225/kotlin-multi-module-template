package kr.co.hyo.domain.post.repository

import kr.co.hyo.config.RedisEmbbededConfig
import kr.co.hyo.domain.post.entity.PostFeed
import kr.co.hyo.domain.post.repository.redistemplate.PostFeedRedisTemplateRepositoryImpl
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
import java.time.LocalDateTime

@DataRedisTest(properties = ["spring.profiles.active=test"])
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(classes = [RedisEmbbededConfig::class, PostFeedRedisTemplateRepositoryImpl::class])
@DisplayName("PostFeedRedisTemplateRepository 단위 테스트")
class PostFeedRedisTemplateRepositoryTests {

    @Autowired
    lateinit var postFeedRedisTemplateRepository: PostFeedRedisTemplateRepository

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
        val key: String = postFeed.getMemberIdFeedsKey()
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()

        // when
        postFeedRedisTemplateRepository.zadd(key = key, value = postId, score = score)

        // then
        val postIds: List<Long> =
            postFeedRedisTemplateRepository.zrevRange(key = key, start = 0, end = 10, clazz = Long::class.java)

        assertThat(postIds).isNotEmpty
        assertThat(postIds.first()).isEqualTo(postId)
    }
}
